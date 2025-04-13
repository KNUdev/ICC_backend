package ua.knu.knudev.employeemanager.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.sql.Time;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WorkHours {
    @Column
    private Time startTime;

    @Column
    private Time endTime;
}
