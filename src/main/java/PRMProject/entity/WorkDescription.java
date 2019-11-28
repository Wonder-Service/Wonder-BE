package PRMProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "TBL_WORK_DESCRIPTION")
public class WorkDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name= "CUSTOMER_ID")
    private long customerId;

}
