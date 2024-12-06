package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CoordinatesDto(

        @Schema(example = "5")
        @JsonProperty(value = "x", required = true)
        Long x,

        @Schema(example = "3")
        @JsonProperty(value = "y", required = true)
        Float y
) {
}
