package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password
) {
}
