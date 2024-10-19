package itmo.is.mapper;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.model.domain.SpaceMarine;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CoordinatesMapper.class, ChapterMapper.class, AstartesCategoryMapper.class}
)
public interface SpaceMarineMapper extends EntityMapper<SpaceMarineDto, SpaceMarine> {
    SpaceMarine toEntity(CreateSpaceMarineRequest request);

    SpaceMarine toEntity(UpdateSpaceMarineRequest request);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<SpaceMarineDto> toDto(List<SpaceMarine> spaceMarines);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<SpaceMarine> toEntities(List<SpaceMarineDto> spaceMarinesDTO);
}
