package PRMProject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "TBL_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "IS_DELETE")
    private boolean isDelete;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "TBL_WORKER_SKILL", joinColumns = @JoinColumn(name = "WORKER_ID"), inverseJoinColumns = @JoinColumn(name = "SKILL_ID"))
    private Set<Skill> skills = new HashSet<>();

    @Column(name = "DEVICE_ID")
    private String deviceId;
}
