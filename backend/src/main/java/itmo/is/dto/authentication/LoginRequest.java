package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty(value = "username", required = true)
        String username,

        @JsonProperty(value = "password", required = true)
        String password
) {
}
