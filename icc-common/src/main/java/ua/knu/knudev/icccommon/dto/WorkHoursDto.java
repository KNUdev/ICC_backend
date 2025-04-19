package ua.knu.knudev.icccommon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;

@Schema(description = "Represents start and end work time")
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WorkHoursDto {
    @Schema(description = "Start of working hours")
    @NotNull(message = "startTime field cannot be null")
    private Time startTime;

    @Schema(description = "End of working hours")
    @NotNull(message = "endTime field cannot be null")
    private Time endTime;
}
