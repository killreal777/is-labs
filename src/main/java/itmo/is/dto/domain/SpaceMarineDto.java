package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.authentication.UserDto;
import itmo.is.model.domain.AstartesCategory;

public record SpaceMarineDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Long id,

        @Schema(example = "Kirill Kravtsov")
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "coordinates", required = true)
        CoordinatesDto coordinates,

        @Schema(example = "100")
        @JsonProperty(value = "health", required = true)
        Double health,

        @Schema(example = "true")
        @JsonProperty(value = "loyal", required = false)
        boolean loyal,

        @Schema(example = "175")
        @JsonProperty(value = "height", required = true)
        Integer height,

        @Schema(example = "HELIX")
        @JsonProperty(value = "category", required = false)
        AstartesCategory category,

        @JsonProperty(value = "owner", required = true)
        UserDto owner,

        @Schema(example = "true")
        @JsonProperty(value = "admin_edit_allowed", required = true)
        boolean adminEditAllowed
) {
}
