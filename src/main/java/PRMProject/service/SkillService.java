package PRMProject.service;

import PRMProject.entity.Skill;

import java.util.List;

public interface SkillService {

    List<Skill> getAll(String workerName);
}
