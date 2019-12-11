package PRMProject.config.mapper;

import PRMProject.entity.Order;
import PRMProject.model.OrderResultDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    public abstract OrderResultDTO toDto(Order entity);
}

