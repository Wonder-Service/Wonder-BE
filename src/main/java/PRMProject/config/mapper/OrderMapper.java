package PRMProject.config.mapper;

import PRMProject.entity.Order;
import PRMProject.model.OrderDTO;
import PRMProject.model.OrderResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Mapping(source = "worker.id", target="workerId")
    public abstract OrderResultDTO toDto(Order entity);
}

