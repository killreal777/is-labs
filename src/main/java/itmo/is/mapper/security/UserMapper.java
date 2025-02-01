package itmo.is.mapper.security;

import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.dto.authentication.UserDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.security.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends EntityMapper<UserDto, User> {
    User toEntity(RegisterRequest registerRequest);
}
