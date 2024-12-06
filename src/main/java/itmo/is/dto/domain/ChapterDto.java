package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.authentication.UserDto;

public record ChapterDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Long id,

        @Schema(example = "Operation Systems Chapter")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "Klimenkov's Legion")
        @JsonProperty(value = "parent_legion", required = false)
        String parentLegion,

        @Schema(example = "1000")
        @JsonProperty(value = "marines_count", required = true)
        long marinesCount,

        @JsonProperty(value = "owner", required = true)
        UserDto owner,

        @Schema(example = "false")
        @JsonProperty(value = "admin_edit_allowed", required = true)
        boolean adminEditAllowed
) {
}
