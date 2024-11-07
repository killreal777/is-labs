package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import itmo.is.dto.authentication.UserDto;
import itmo.is.model.domain.AstartesCategory;

public record SpaceMarineDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name")
        String name,

        @JsonProperty("coordinates")
        CoordinatesDto coordinates,

        @JsonProperty("chapter")
        ChapterDto chapter,

        @JsonProperty("health")
        Double health,

        @JsonProperty("loyal")
        boolean loyal,

        @JsonProperty("height")
        Integer height,

        @JsonProperty("category")
        AstartesCategory category,

        @JsonProperty("owner")
        UserDto owner,

        @JsonProperty("admin_edit_allowed")
        boolean adminEditAllowed
) {
}
