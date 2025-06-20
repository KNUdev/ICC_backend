package ua.knu.knudev.employeemanagerapi.response;

import lombok.Builder;
import ua.knu.knudev.employeemanagerapi.dto.ShortEmployeeDto;

@Builder
public record AccountReceivingResponse(
        ShortEmployeeDto shortEmployeeDto,
        String responseMessage
) {
}
