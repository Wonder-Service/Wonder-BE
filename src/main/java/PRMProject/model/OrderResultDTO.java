package PRMProject.model;

import PRMProject.config.request.WorkDescriptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderResultDTO {
    private Long id;
    private WorkDescriptionDto workDescription;
    private BigDecimal totalCredit;
    private String nameDevice;
    private String address;
    private Float lat;
    private Float lng;
    private String detailAddress;
    private int rate;
    private String status;
    private String feedback;
}
