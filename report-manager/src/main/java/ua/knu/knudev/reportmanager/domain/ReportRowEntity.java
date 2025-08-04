package ua.knu.knudev.reportmanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "report_row")
@Getter
@Setter
public class ReportRowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "points", nullable = false)
    private BigDecimal points;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "position")
    private String position;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "contract_valid_to")
    private LocalDate contractValidTo;

    public ReportRowEntity() {
    }
}
