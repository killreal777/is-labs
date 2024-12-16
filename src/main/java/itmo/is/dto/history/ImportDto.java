package itmo.is.dto.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.is.dto.authentication.UserDto;

import java.time.LocalDateTime;

public record ImportDto(
        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Long id,

        @JsonProperty(value = "user", required = true)
        UserDto user,

        @Schema(example = "true")
        @JsonProperty(value = "success", required = true)
        boolean success,

        @Schema(example = "5")
        @JsonProperty(value = "objects_added", required = true)
        Integer objectsAdded,

        @Schema(example = "2024-11-30T20:24:23.760031")
        @JsonProperty(value = "started_at", required = true)
        LocalDateTime startedAt,

        @Schema(example = "2024-11-30T20:24:24.233199")
        @JsonProperty(value = "finished_at", required = true)
        LocalDateTime finishedAt
) {
}
