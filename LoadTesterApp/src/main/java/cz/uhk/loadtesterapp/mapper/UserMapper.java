package cz.uhk.loadtesterapp.mapper;

import cz.uhk.loadtesterapp.model.dto.UserCreateRequest;
import cz.uhk.loadtesterapp.model.dto.UserDto;
import cz.uhk.loadtesterapp.model.dto.UserUpdateRequest;
import cz.uhk.loadtesterapp.model.entity.User;
import org.mapstruct.*;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    User toEntity(UserCreateRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserUpdateRequest req, @MappingTarget User entity);
}