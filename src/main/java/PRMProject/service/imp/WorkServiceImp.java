package PRMProject.service.imp;

import PRMProject.config.request.WorkDescriptionDto;
import PRMProject.entity.User;
import PRMProject.entity.WorkDescription;
import PRMProject.repository.UserRepository;
import PRMProject.repository.WorkRepository;
import PRMProject.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class WorkServiceImp implements WorkService {

    @Autowired
    WorkRepository workRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public WorkDescription createWork(WorkDescriptionDto workDescriptionDto) {
        if(StringUtils.isEmpty(workDescriptionDto.getCustomerId())){
            log.error("Customer ID required");
            return null;
        }
        Optional<User> user = userRepository.findById(workDescriptionDto.getCustomerId());
        if (!user.isPresent()) {
            log.error("Customer data not found");
            return null;
        }

        WorkDescription workDescription = new WorkDescription();
        workDescription.setCustomerId(user.get().getId());
        workDescription.setDescription(workDescriptionDto.getDescription());
        return workRepository.save(workDescription);
    }
}
