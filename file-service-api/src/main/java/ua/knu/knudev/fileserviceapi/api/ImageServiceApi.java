package ua.knu.knudev.fileserviceapi.api;

import org.springframework.http.ResponseEntity;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;

public interface ImageServiceApi extends BaseFileServiceApi<ImageSubfolder> {

    ResponseEntity<byte[]> getFile(String filename, ImageSubfolder imageSubfolder);
}
