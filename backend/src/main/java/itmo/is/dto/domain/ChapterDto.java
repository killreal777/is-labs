package itmo.is.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import itmo.is.dto.authentication.UserDto;

public record ChapterDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name")
        String name,

        @JsonProperty("parent_legion")
        String parentLegion,

        @JsonProperty("marines_count")
        long marinesCount,

        @JsonProperty("owner")
        UserDto owner,

        @JsonProperty("admin_edit_allowed")
        boolean adminEditAllowed
) {
}
