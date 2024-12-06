package itmo.is.mapper.history;

import itmo.is.dto.history.ImportLogDto;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.history.ImportLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ImportLogMapper {
    ImportLogDto toDto(ImportLog entity);
}
