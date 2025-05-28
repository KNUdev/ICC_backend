package ua.knu.knudev.applicationmanagerapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(name = "DepartmentGetAllRequest", description = "Pagination request for departments")
public record DepartmentGetAllRequest(

        @Min(0)
        @Schema(description = "Page number", example = "0", requiredMode = REQUIRED)
        int page,

        @Min(1)
        @Schema(description = "Page size", example = "10", requiredMode = REQUIRED)
        int size
) {
}