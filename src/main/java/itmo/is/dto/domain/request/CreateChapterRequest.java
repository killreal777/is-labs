package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateChapterRequest(

        @Schema(example = "Operation Systems Chapter")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "Klimenkov's Legion")
        @JsonProperty(value = "parentLegion", required = false)
        String parentLegion,

        @Schema(example = "false")
        @JsonProperty(value = "adminEditAllowed", required = true)
        boolean adminEditAllowed
) {
}
