package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EnrollRequest(
        @JsonProperty(value = "space_marine_id", required = true)
        Long spaceMarineId,

        @JsonProperty(value = "chapter_id", required = true)
        Long chapterId
) {
}
