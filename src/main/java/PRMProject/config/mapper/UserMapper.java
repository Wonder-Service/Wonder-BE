package PRMProject.config.mapper;

import PRMProject.entity.User;
import PRMProject.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserDto toDto(User entity);

}

