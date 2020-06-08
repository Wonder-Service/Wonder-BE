package PRMProject.config.request;

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
public class WorkDescriptionDto {
    private String description;

    private Long customerId;

    private Long skillId;

    private Date dateCreated;

}
