package ua.knu.knudev.reportmanager.enums;

public enum ReportFields {
    ID("ІД", 1500),
    NameSurname("Ім'я та прізвище", 12000),
    PhoneNumber("Номер телефону", 8000),
    Email("Пошта", 8000),
    Position("Посада", 4000),
    Salary("Зарплата", 4000),
    ContractValidTo("Дата закінчення контракту", 4000);

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
