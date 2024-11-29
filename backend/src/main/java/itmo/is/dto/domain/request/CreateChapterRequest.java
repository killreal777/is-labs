package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateChapterRequest(

        @Schema(example = "Operation Systems Chapter")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "Klimenkov's Legion")
        @JsonProperty(value = "parent_legion", required = false)
        String parentLegion,

        @Schema(example = "false")
        @JsonProperty(value = "admin_edit_allowed", required = true)
        boolean adminEditAllowed
) {
}
