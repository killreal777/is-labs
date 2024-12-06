package itmo.is.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Long id,

        @Schema(example = "killreal777")
        @JsonProperty(value = "username", required = true)
        String username
) {
}
