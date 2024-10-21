package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateChapterRequest(
        @JsonProperty("id")
        Long id,

        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "parent_legion", required = false)
        String parentLegion,

        @JsonProperty(value = "admin_edit_allowed", required = true)
        boolean adminEditAllowed
) {
}
