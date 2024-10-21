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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public JwtResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userRepository.findByUsername(request.username()).orElseThrow();

        if (!user.isEnabled()) {
            throw new AuthenticationServiceException("User is disabled");
        }

        return generateJwt(user);
    }

    public JwtResponse registerUser(RegisterRequest request) {
        return registerEnabled(request, Role.ROLE_USER);
    }

    public boolean hasRegisteredAdmins() {
        return userRepository.existsByRole(Role.ROLE_ADMIN);
    }

    public JwtResponse registerFirstAdmin(RegisterRequest request) {
        if (hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is already registered");
        }
        return registerEnabled(request, Role.ROLE_ADMIN);
    }

    public void submitAdminRegistrationRequest(RegisterRequest registerRequest) {
        if (!hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is not registered yet");
        }
        boolean enabled = false;
        createUser(registerRequest, Role.ROLE_ADMIN, enabled);
    }

    public void approveAdminRegistrationRequest(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void rejectAdminRegistrationRequest(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        if (user.isEnabled()) {
            throw new AuthenticationServiceException("User is enabled");
        }
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
        checkIfUsernameIsTaken(request.username());
        User user = userMapper.toEntity(request);
        encodePassword(user);
        user.setRole(role);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void checkIfUsernameIsTaken(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthenticationServiceException("Username " + username + " is taken");
        }
    }

    private JwtResponse generateJwt(User user) {
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }
}
