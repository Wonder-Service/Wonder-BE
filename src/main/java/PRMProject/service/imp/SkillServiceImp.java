package PRMProject.service.imp;

import PRMProject.config.mapper.SkillMapper;
import PRMProject.entity.Skill;
import PRMProject.entity.Skill_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.SkillDto;
import PRMProject.repository.SkillRepository;
import PRMProject.repository.UserRepository;
import PRMProject.service.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SkillServiceImp implements SkillService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Override
    public List<SkillDto> getAll(String skillName) {
        List<Specification<Skill>> specification = new ArrayList<>();
        if (!StringUtils.isEmpty(skillName)) {
            specification.add((root, query, cb) -> {
                return cb.like(cb.upper(root.get(Skill_.name)), "%" + skillName.toUpperCase().trim() + "%", '\\');
            });
        }
        return skillRepository.findAll(SpecificationBuilder.build(specification)).stream().collect(Collectors.toList()).stream().map(skillMapper::toDto).collect(Collectors.toList());
    }


}
