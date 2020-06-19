package PRMProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UserRegisterDto {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
    private String fullname;
    private String address;
    private Set<Long> skills;

    @JsonProperty("isDelete")
    private boolean isDelete;

    private String deviceId;
    private float rate;
}
