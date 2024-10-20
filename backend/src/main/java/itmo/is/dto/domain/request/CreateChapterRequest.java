package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateChapterRequest(
        @JsonProperty("name")
        String name,

        @JsonProperty("parent_legion")
        String parentLegion
) {
}
