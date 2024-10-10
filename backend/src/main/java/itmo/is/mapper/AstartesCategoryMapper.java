package itmo.is.mapper;

import itmo.is.dto.domain.AstartesCategoryDto;
import itmo.is.model.domain.AstartesCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AstartesCategoryMapper extends EntityMapper<AstartesCategoryDto, AstartesCategory> {
}
