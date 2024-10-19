package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpaceMarineDto(
        Long id,
        String name,
        CoordinatesDto coordinates,
        ChapterDto chapter,
        Double health,
        boolean loyal,
        Integer height,
        AstartesCategoryDto category,
        @JsonProperty("admin_edit_allowed")
        boolean adminEditAllowed
) {
}
