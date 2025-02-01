package itmo.is.mapper.history;

import itmo.is.dto.history.ImportDto;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.history.Import;
import org.mapstruct.Mapper;

@Mapper(uses = {UserMapper.class})
public interface ImportMapper {
    ImportDto toDto(Import entity);
}
