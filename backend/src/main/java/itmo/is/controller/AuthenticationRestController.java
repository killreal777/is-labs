package itmo.is.controller;
import itmo.is.dto.authentication.LoginRequest;
import itmo.is.dto.authentication.JwtResponse;
import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.dto.authentication.UserDto;
import itmo.is.service.security.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/auth/register/admin")
    public ResponseEntity<JwtResponse> registerAdmin(@RequestBody RegisterRequest request) {
        if (authenticationService.hasRegisteredAdmins()) {
            authenticationService.submitAdminRegistrationRequest(request);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(authenticationService.registerFirstAdmin(request));
        }
    }

    @PutMapping("admin/registration-requests/{userId}")
    public ResponseEntity<Void> approveAdminRegistrationRequest(@PathVariable Long userId) {
        authenticationService.approveAdminRegistrationRequest(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("admin/registration-requests/{userId}")
    public ResponseEntity<Void> rejectAdminRegistrationRequest(@PathVariable Long userId) {
        authenticationService.rejectAdminRegistrationRequest(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("admin/registration-requests")
    public ResponseEntity<Page<UserDto>> getPendingRegistrationRequests(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(authenticationService.getPendingRegistrationRequests(pageable));
    }
}