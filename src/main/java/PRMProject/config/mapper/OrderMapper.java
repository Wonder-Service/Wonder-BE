package PRMProject.config.mapper;

import PRMProject.entity.Order;
import PRMProject.model.OrderDTO;
import PRMProject.model.OrderResultDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Mappings({
            @Mapping(source = "worker.id", target="workerId"),
            @Mapping(source = "workDescription.customer", target = "customer")
    })
    public abstract OrderResultDTO toDto(Order entity);
}

