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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "TBL_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKER_ID")
    private User worker;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_DESCRIPTION_ID")
    private WorkDescription workDescription;

    @Column(name = "TOTAL_CREDIT")
    private BigDecimal totalCredit;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RATE")
    private int rate;

    @Column(name = "FEEDBACK")
    private String feedback;

    @Column(name = "NAME_DEVICE")
    private String nameDevice;

    @Column(name = "LATITUDE")
    private Float lat;

    @Column(name= "LONGITUDE")
    private Float lng;

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;
}
