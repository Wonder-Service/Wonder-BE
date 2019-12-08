package PRMProject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderCancelTrackingDto {
    private Long id;
    private Long userId;
    private Long orderId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateCancelled;
}
