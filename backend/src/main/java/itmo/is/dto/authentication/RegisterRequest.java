package itmo.is.dto.authentication;

import itmo.is.model.security.Role;

public record RegisterRequest(String username, String password, Role role) {
}
