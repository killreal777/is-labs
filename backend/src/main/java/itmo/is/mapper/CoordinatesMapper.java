package itmo.is.mapper;

import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.model.domain.Coordinates;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoordinatesMapper extends EntityMapper<CoordinatesDto, Coordinates> {
}
