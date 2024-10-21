package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.model.domain.AstartesCategory;

public record UpdateSpaceMarineRequest(
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "coordinates", required = true)
        CoordinatesDto coordinates,

        @JsonProperty(value = "health", required = true)
        Double health,

        @JsonProperty(value = "loyal", required = false)
        boolean loyal,

        @JsonProperty(value = "height", required = true)
        Integer height,

        @JsonProperty(value = "category", required = false)
        AstartesCategory category
) {
}
