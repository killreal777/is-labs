package itmo.is.mapper.domain;

import itmo.is.dto.domain.CoordinatesDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Coordinates;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoordinatesMapper extends EntityMapper<CoordinatesDto, Coordinates> {
}
