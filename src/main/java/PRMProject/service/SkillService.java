package PRMProject.service;

import PRMProject.entity.Skill;
import PRMProject.model.SkillDto;

import java.util.List;

public interface SkillService {

    List<SkillDto> getAll(String workerName);
}
