package PRMProject.model;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestOrderDTO {

    private String description;
    private String address;
    private long skillId;
    private String nameDevice;
    @JsonValue
    private Coords coords;
    private String detailAddress;

}
