package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateChapterRequest(

        @Schema(example = "Software Engineering Chapter")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "ITMO Legion")
        @JsonProperty(value = "parent_legion", required = false)
        String parentLegion
) {
}
