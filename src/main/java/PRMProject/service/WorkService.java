package PRMProject.service;

import PRMProject.config.request.WorkDescriptionDto;
import PRMProject.entity.WorkDescription;

public interface WorkService {

    WorkDescription createWork(WorkDescriptionDto workDescriptionDto);
}
