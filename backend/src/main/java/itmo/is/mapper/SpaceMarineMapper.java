package itmo.is.mapper;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.model.domain.SpaceMarine;
import org.mapstruct.Mapper;


@Mapper(
        componentModel = "spring",
        uses = {CoordinatesMapper.class, ChapterMapper.class, AstartesCategoryMapper.class}
)
public interface SpaceMarineMapper extends EntityMapper<SpaceMarineDto, SpaceMarine> {
    SpaceMarine toEntity(CreateSpaceMarineRequest request);

    SpaceMarine toEntity(UpdateSpaceMarineRequest request);
}
