package ua.knu.knudev.reportmanager.generator;

import ua.knu.knudev.reportmanagerapi.dto.ReportRowDto;
import java.io.OutputStream;
import java.util.List;

public interface ReportGenerator {
    void generate(List<ReportRowDto> data, OutputStream out);
}
