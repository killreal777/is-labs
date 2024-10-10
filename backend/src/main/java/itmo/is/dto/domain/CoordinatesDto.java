package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordinatesDto(
        Long x,
        Float y
) {
}
