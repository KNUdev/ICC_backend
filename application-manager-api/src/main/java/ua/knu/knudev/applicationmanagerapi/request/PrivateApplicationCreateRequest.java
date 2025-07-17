package ua.knu.knudev.applicationmanagerapi.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.icccommon.dto.FullNameDto;
import ua.knu.knudev.icccommon.enums.ApplicationStatus;

@Builder
@Schema(name = "PrivateApplicationCreateRequest", description = "Object containing data required to create a new private application")
public record PrivateApplicationCreateRequest(

        @Schema(description = "Name of the applicant",
                implementation = FullNameDto.class,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "This field cannot be null")
        FullNameDto applicantName,

        @Schema(description = "Email of the applicant",
                example = "jane.smith@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "This field cannot be null")
        String applicantEmail,

        @Schema(description = "Description of the reported problem",
                example = "Air conditioning not working in office",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 3000)
        @NotBlank(message = "This field cannot be null")
        @Size(max = 3000)
        String problemDescription,

        @Schema(description = "Photo of the reported problem (URL or file path)",
                example = "http://example.com/images/ac-issue.jpg",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String problemPhotoName,

        @Schema(description = "Photo which was uploaded to describe the problem",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        MultipartFile problemPhoto,

        @Schema(description = "Initial status of the application",
                example = "IN_QUEUE",
                implementation = ApplicationStatus.class,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "This field cannot be null")
        ApplicationStatus status

) {
}
