package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExpelRequest(
        @JsonProperty("space_marine_id")
        Long spaceMarineId
) {
}
