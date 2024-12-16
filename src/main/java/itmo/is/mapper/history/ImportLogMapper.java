package itmo.is.mapper.history;

import itmo.is.dto.history.ImportLogDto;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.history.Import;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ImportLogMapper {
    ImportLogDto toDto(Import entity);
}
