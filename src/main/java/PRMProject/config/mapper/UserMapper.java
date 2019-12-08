package PRMProject.config.mapper;

import PRMProject.entity.User;
import PRMProject.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Mapping(source= "delete", target = "isDelete")
    public abstract UserDto toDto(User entity);

}

