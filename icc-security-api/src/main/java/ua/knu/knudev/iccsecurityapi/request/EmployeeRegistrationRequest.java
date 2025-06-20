package ua.knu.knudev.iccsecurityapi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

@Builder
public record EmployeeRegistrationRequest(
        @NotBlank(message = "Email can`t be blank or empty")
        @Email(message = "The string must be in email format")
        @Pattern(
                regexp = "^[\\w.-]+@knu\\.ua$",
                message = "Email must be in @knu.ua domain"
        )
        String email,

        @NotBlank(message = "Password can`t be blank or empty")
        @Size(
                min = 8,
                max = 64,
                message = "Password size must be in the gap from 8 to 64 symbols"
        )
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$",
                message = "Password must contains only allowed symbols"
        )
        String password,

        @NotBlank
        EmployeeAdministrativeRole role
) {
}
