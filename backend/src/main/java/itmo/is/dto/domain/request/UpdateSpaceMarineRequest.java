package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.model.domain.AstartesCategory;

public record UpdateSpaceMarineRequest(

        @Schema(example = "Alexey Volkov")
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "coordinates", required = true)
        CoordinatesDto coordinates,

        @Schema(example = "90")
        @JsonProperty(value = "health", required = true)
        Double health,

        @Schema(example = "false")
        @JsonProperty(value = "loyal", required = false)
        boolean loyal,

        @Schema(example = "183")
        @JsonProperty(value = "height", required = true)
        Integer height,

        @Schema(example = "TERMINATOR")
        @JsonProperty(value = "category", required = false)
        AstartesCategory category
) {
}
