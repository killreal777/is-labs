package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordinatesDto(
        @JsonProperty("x")
        Long x,

        @JsonProperty("y")
        Float y
) {
}
