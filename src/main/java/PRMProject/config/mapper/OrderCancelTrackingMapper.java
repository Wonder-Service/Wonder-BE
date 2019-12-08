package PRMProject.config.mapper;

import PRMProject.entity.OrderCancelTracking;
import PRMProject.model.OrderCancelTrackingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderCancelTrackingMapper {
    public abstract OrderCancelTrackingDto toDto(OrderCancelTracking entity);

    public abstract OrderCancelTracking toEntity(OrderCancelTrackingDto dto);

}

