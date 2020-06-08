package PRMProject.config.mapper;

import PRMProject.entity.Skill;
import PRMProject.model.SkillDto;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class SkillMapper {
    public abstract SkillDto toDto(Skill entity);

    public abstract Set<SkillDto> toDto(Set<Skill> entities);
}

