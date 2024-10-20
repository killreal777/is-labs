package itmo.is.controller;
import itmo.is.dto.authentication.LoginRequest;
import itmo.is.dto.authentication.JwtResponse;
import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.dto.authentication.UserDto;
import itmo.is.service.security.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("admin/register")
    public ResponseEntity<JwtResponse> registerAdmin(@RequestBody RegisterRequest request) {
        if (authenticationService.hasRegisteredAdmins()) {
            authenticationService.submitAdminRegistrationRequest(request);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(authenticationService.registerFirstAdmin(request));
        }
    }

    @PutMapping("admin/register/{userId}")
    public ResponseEntity<Void> approveAdminRegistrationRequest(@PathVariable Long userId) {
        authenticationService.approveAdminRegistrationRequest(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("admin/register/{userId}")
    public ResponseEntity<Void> rejectAdminRegistrationRequest(@PathVariable Long userId) {
        authenticationService.rejectAdminRegistrationRequest(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("admin/register")
    public ResponseEntity<List<UserDto>> getPendingRegistrationRequests() {
        return ResponseEntity.ok(authenticationService.getPendingRegistrationRequests());
    }
}