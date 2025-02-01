package itmo.is.mapper.domain;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.SpaceMarine;
import org.mapstruct.Mapper;


@Mapper(uses = {CoordinatesMapper.class, ChapterMapper.class})
public interface SpaceMarineMapper extends EntityMapper<SpaceMarineDto, SpaceMarine> {
    SpaceMarine toEntity(CreateSpaceMarineRequest request);

    SpaceMarine toEntity(UpdateSpaceMarineRequest request);
}
