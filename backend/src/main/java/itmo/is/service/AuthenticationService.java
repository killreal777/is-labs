package itmo.is.service;

import itmo.is.dto.authentication.LoginRequest;
import itmo.is.dto.authentication.JwtResponse;
import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.model.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse register(RegisterRequest request) {
        checkIfUsernameIsTaken(request.username());
        User user = toUser(request);
        user = userService.createUser(user);
        String jwt = jwtService.generateToken(Map.of("role", request.role()), user);
        return new JwtResponse(jwt);
    }

    private void checkIfUsernameIsTaken(String username) {
        if (userService.isUserExist(username)) {
            throw new IllegalStateException("Username " + username + " is taken");
        }
    }

    private User toUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(registerRequest.role())
                .build();
    }

    public JwtResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userService.getUserByUsername(request.username());
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }
}
