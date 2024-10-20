package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}

