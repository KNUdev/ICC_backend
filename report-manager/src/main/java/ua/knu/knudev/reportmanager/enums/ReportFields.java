package ua.knu.knudev.reportmanager.enums;

public enum ReportFields {
    ID("ІД", 1500),
    NAME_SURNAME("Ім'я та прізвище", 12000),
    PHONE_NUMBER("Номер телефону", 8000),
    EMAIL("Пошта", 8000),
    POSITION("Посада", 4000),
    SALARY("Заробітня плата", 4000),
    CONTRACT_VALID_TO("Дата закінчення контракту", 4000);

    private final String value;
    private final int width;

    ReportFields(String value, int width) {
        this.value = value;
        this.width = width;
    }

    public String getLabel() {
        return value;
    }

    public int getWidth() {
        return width;
    }
}
