package itmo.is.service.security.authentication;

import itmo.is.dto.authentication.LoginRequest;
import itmo.is.dto.authentication.JwtResponse;
import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.dto.authentication.UserDto;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import itmo.is.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.security.unique-password-constraint}")
    private boolean uniquePasswordConstraint;

    public JwtResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = findUserByUsername(request.username());
        validateUserEnabled(user);
        return generateJwt(user);
    }

    public JwtResponse registerUser(RegisterRequest request) {
        return registerEnabled(request, Role.ROLE_USER);
    }

    public boolean hasRegisteredAdmins() {
        return userRepository.existsByRole(Role.ROLE_ADMIN);
    }

    public JwtResponse registerFirstAdmin(RegisterRequest request) {
        validateFirstAdminNotRegistered();
        return registerEnabled(request, Role.ROLE_ADMIN);
    }

    public void submitAdminRegistrationRequest(RegisterRequest registerRequest) {
        validateFirstAdminRegistered();
        boolean enabled = false;
        createUser(registerRequest, Role.ROLE_ADMIN, enabled);
    }

    public void approveAdminRegistrationRequest(Long userId) {
        var user = findUserById(userId);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void rejectAdminRegistrationRequest(Long userId) {
        var user = findUserById(userId);
        validateUserNotEnabled(user);
        userRepository.delete(user);
    }

    public Page<UserDto> getPendingRegistrationRequests(Pageable pageable) {
        return userRepository.findAllByEnabledFalse(pageable).map(userMapper::toDto);
    }

    private JwtResponse registerEnabled(RegisterRequest request, Role role) {
        boolean enabled = true;
        var user = createUser(request, role, enabled);
        return generateJwt(user);
    }

    private User createUser(RegisterRequest request, Role role, boolean enabled) {
        validateRegisterRequest(request);
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    private JwtResponse generateJwt(User user) {
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationServiceException("User not found with id: " + userId));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private void validateUserEnabled(User user) {
        if (!user.isEnabled()) {
            throw new AuthenticationServiceException("User is disabled: " + user.getUsername());
        }
    }

    private void validateUserNotEnabled(User user) {
        if (user.isEnabled()) {
            throw new AuthenticationServiceException("Cannot delete an enabled user");
        }
    }

    private void validateFirstAdminNotRegistered() {
        if (hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is already registered");
        }
    }

    private void validateFirstAdminRegistered() {
        if (!hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is not registered yet");
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
        validateUsername(request.username());
        validatePassword(request.password());
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthenticationServiceException("Username " + username + " is taken");
        }
    }

    private void validatePassword(String password) { // ТЗ XD
        if (uniquePasswordConstraint) {
            String encodedPassword = passwordEncoder.encode(password);
            userRepository.findByPassword(encodedPassword).ifPresent((user) -> {
                String message = "Password '" + password + "' is already taken by user '" + user.getUsername() + "'";
                throw new AuthenticationServiceException(message);
            });
        }
    }
}
