package ua.knu.knudev.iccrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageProxyController {

    private final ImageServiceApi imageServiceApi;

    @GetMapping("/employee/avatars/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {

        return imageServiceApi.getFile(filename, ImageSubfolder.EMPLOYEE_AVATARS);
    }

    @GetMapping("/{subfolder}/{filename}")
    public ResponseEntity<byte[]> getFile(
            @PathVariable String subfolder,
            @PathVariable String filename
    ) {
        ImageSubfolder folderEnum = ImageSubfolder.valueOf(subfolder.toUpperCase());

        return imageServiceApi.getFile(filename, folderEnum);
    }
}
