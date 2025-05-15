package ua.knu.knudev.integrationtests.utils.constants;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.icccommon.constant.EmployeeAdministrativeRole;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeTestsConstants {

    public static final String TEST_EMPLOYEE_FIRST_NAME = "EmployeeFirstName";
    public static final String TEST_EMPLOYEE_MIDDLE_NAME = "EmployeeMiddleName";
    public static final String TEST_EMPLOYEE_LAST_NAME = "EmployeeLastName";
    public static final String TEST_EMPLOYEE_EMAIL = "ivan@knu.ua";
    public static final String TEST_EMPLOYEE_PHONE_NUMBER = "380960000000";
    public static final LocalDateTime TEST_EMPLOYEE_CREATED_AT = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
    public static final LocalDateTime TEST_EMPLOYEE_UPDATED_AT = LocalDateTime.of(2020, 1, 1, 0, 0);
    public static final Double TEST_EMPLOYEE_SALARY_IN_UAH = 18000.;
    public static final Boolean TEST_EMPLOYEE_IS_STUDENT = true;
    public static final MultipartFile TEST_AVATAR_FILE = new MockMultipartFile("avatar.jpg", "avatar.jpeg", "image/jpeg", "image".getBytes());
    public static final LocalDate TEST_EMPLOYEE_CONTRACT_END_DATE = LocalDate.of(2026, 1, 1);
    public static final Time TEST_EMPLOYEE_WORK_START_TIME = Time.valueOf("09:00:00");
    public static final Time TEST_EMPLOYEE_WORK_END_TIME = Time.valueOf("16:00:00");
    public static final EmployeeAdministrativeRole TEST_EMPLOYEE_ROLE = EmployeeAdministrativeRole.COMMON_USER;


    public static final String TEST_SECTOR_NAME_IN_ENGLISH = "test-sector-name";
    public static final String TEST_SECTOR_NAME_IN_UKRAINIAN = "тестове-ім'я-сектора";

    public static final String TEST_SPECIALTY_NAME_IN_ENGLISH = "test-specialty-name";
    public static final String TEST_SPECIALTY_NAME_IN_UKRAINIAN = "тестове-ім'я-спеціальності";
}