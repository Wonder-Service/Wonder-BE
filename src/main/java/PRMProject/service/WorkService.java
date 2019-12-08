package PRMProject.service;

import PRMProject.config.request.WorkDescriptionDto;
import PRMProject.entity.WorkDescription;

import java.util.List;

public interface WorkService {

    WorkDescription createWork(WorkDescriptionDto workDescriptionDto);

    List<WorkDescription> getAll();
}
