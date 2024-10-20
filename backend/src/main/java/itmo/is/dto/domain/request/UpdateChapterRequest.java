package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateChapterRequest(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name")
        String name,

        @JsonProperty("parent_legion")
        String parentLegion,

        @JsonProperty("admin_edit_allowed")
        boolean adminEditAllowed
) {
}
