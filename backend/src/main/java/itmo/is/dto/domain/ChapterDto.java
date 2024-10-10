package itmo.is.dto.domain;

public record ChapterDto(
        Long id,
        String name,
        String parentLegion,
        long marinesCount
) {
}
