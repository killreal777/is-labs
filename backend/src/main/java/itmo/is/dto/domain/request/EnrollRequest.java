package itmo.is.dto.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EnrollRequest(
        @JsonProperty("space_marine_id")
        Long spaceMarineId,

        @JsonProperty("chapter_id")
        Long chapterId
) {
}
