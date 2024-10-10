package itmo.is.dto.domain.request;

import itmo.is.dto.domain.AstartesCategoryDto;
import itmo.is.dto.domain.CoordinatesDto;

public record CreateSpaceMarineRequest(
        String name,
        CoordinatesDto coordinates,
        Double health,
        boolean loyal,
        Integer height,
        AstartesCategoryDto category
) {
}
