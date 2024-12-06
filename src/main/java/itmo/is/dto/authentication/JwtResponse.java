package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record JwtResponse(

        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraWxscmVhbDc3NyIsImlhdCI6MTczMjg2MTEwNywiZXhwIjoxNzMyOTQ3NTA3fQ.qzKFP5gSrASGLjACOhydbhV7famSFSllK1xUjk8iXRg")
        @JsonProperty(value = "access_token", required = true)
        String accessToken
) {
}

