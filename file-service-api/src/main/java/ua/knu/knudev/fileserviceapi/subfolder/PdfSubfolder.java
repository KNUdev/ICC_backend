package ua.knu.knudev.fileserviceapi.subfolder;

//import ua.knu.knudev.knudevcommon.constant.AccountTechnicalRole;
//import ua.knu.knudev.knudevcommon.constant.LearningUnit;

public enum PdfSubfolder implements PdfSubfolderI {
    EDUCATION_PROGRAM_PROGRAM_TASKS("/education/programs/programs/tasks"),
    EDUCATION_PROGRAM_SECTION_TASKS("/education/programs/sections/tasks"),
    EDUCATION_PROGRAM_MODULE_TASKS("/education/programs/modules/tasks"),
    EDUCATION_PROGRAM_TOPIC_TASKS("/education/programs/topics/tasks"),

    //todo rename. This is for role elevation
    ROLE_ASSIGNMENTS_TASK("/tasks/campus/%s-role-tasks");

    private final String pathPattern;

    PdfSubfolder(String pattern) {
        this.pathPattern = pattern;
    }

    @Override
    public String getSubfolderPath() {
//        todo
//        return "todo";
        return pathPattern;
    }

    /**
     * Only valid if this == ROLE_TASKS; otherwise throws
//     */
    // todo maybe delete
//    public PdfSubfolderI forRole(AccountTechnicalRole technicalRole) {
//        if (this != ROLE_ASSIGNMENTS_TASK) {
//            throw new UnsupportedOperationException(
//                    "forRole(...) only applicable to ROLE_TASKS"
//            );
//        }
//        // Return a separate PdfSubfolderI that formats the path dynamically
//        return new RolePdfSubfolder(pathPattern, technicalRole);
//    }
//
//    public static PdfSubfolder getFromLearningUnit(LearningUnit learningUnit) {
//        return switch (learningUnit) {
//            case PROGRAM -> EDUCATION_PROGRAM_PROGRAM_TASKS;
//            case SECTION -> EDUCATION_PROGRAM_SECTION_TASKS;
//            case MODULE -> EDUCATION_PROGRAM_MODULE_TASKS;
//            case TOPIC -> EDUCATION_PROGRAM_TOPIC_TASKS;
//        };
//    }
//
//    private record RolePdfSubfolder(
//            String pathPattern,
//            AccountTechnicalRole technicalRole
//    ) implements PdfSubfolderI {
//
//        @Override
//        public String getSubfolderPath() {
//            return String.format(pathPattern, technicalRole.getRoleId());
//        }
//    }
}
