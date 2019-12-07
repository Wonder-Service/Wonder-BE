package PRMProject.model;

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
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;
    private Set<SkillDto> skills;
    private boolean isDelete;
    private String deviceId;
}
