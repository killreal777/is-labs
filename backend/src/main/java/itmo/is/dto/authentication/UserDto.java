package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password
) {
}
