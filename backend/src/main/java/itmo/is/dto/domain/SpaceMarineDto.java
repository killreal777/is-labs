package itmo.is.dto.domain;

public record SpaceMarineDto(
        Long id,
        String name,
        CoordinatesDto coordinates,
        ChapterDto chapter,
        Double health,
        boolean loyal,
        Integer height,
        AstartesCategoryDto category
) {
}
