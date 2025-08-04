package ua.knu.knudev.icccommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReportFields {
    ID("Унікальний ідентифікатор", 9500),
    NAME_SURNAME("Ім'я та прізвище", 10000),
    PHONE_NUMBER("Номер телефону", 6000),
    EMAIL("Пошта", 7000),
    POSITION("Посада", 12000),
    SALARY("Заробітня плата", 6000),
    CONTRACT_VALID_TO("Дата закінчення контракту", 9000);

    @Getter
    private final String value;
    @Getter
    private final int width;
}
