package ua.knu.knudev.applicationmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ua.knu.knudev.applicationmanagerapi.devprofile.DevProfileApplicationManagerApi;
import ua.knu.knudev.applicationmanagerapi.dto.DepartmentDto;
import ua.knu.knudev.icccommon.dto.MultiLanguageFieldDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class DevProfileApplicationManager implements DevProfileApplicationManagerApi {

    private static final String ENGLISH_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String UKRAINIAN_LETTERS = "абвгґдеєжзийіїклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИЙІЇКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    private static final Random RANDOM = new Random();
    private final DepartmentService departmentService;

    @Override
    public List<DepartmentDto> createTestDepartments(int numberOfDepartments) {
        List<DepartmentDto> departments = new ArrayList<>();

        for (int i = 0; i < numberOfDepartments; i++) {
            MultiLanguageFieldDto name =
                    new MultiLanguageFieldDto(
                            "department " + getRandomString(ENGLISH_LETTERS),
                            "департамент " + getRandomString(UKRAINIAN_LETTERS)
                    );
            DepartmentDto department = departmentService.create(name);

            departments.add(department);
        }

        return departments;
    }

    private String getRandomString(String letters) {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(letters.charAt(RANDOM.nextInt(letters.length())));
        }
        return sb.toString();
    }
}
