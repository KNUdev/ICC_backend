//package ua.knu.knudev.fileservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import ua.knu.knudev.fileservice.adapter.FileUploadAdapter;
//import ua.knu.knudev.fileserviceapi.dto.FileUploadPayload;
//import ua.knu.knudev.fileserviceapi.dto.FolderPath;
//import ua.knu.knudev.fileserviceapi.folder.FileFolderProperties;
//import ua.knu.knudev.fileserviceapi.subfolder.FileSubfolder;
//import ua.knu.knudev.knudevcommon.exception.FileException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Set;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class FileService {
//
//    private static final String FILE_EXTENSION_SEPARATOR = ".";
//    private static final String PATH_TRAVERSAL_TOKEN = "..";
//    private static final String ALLOWED_FILENAME_CHARACTERS_PATTERN = "^[a-zA-Z0-9]+$";
//
//    protected final FileUploadAdapter fileUploadAdapter;
//
//    public String uploadFile(MultipartFile file,
//                             final String customFilename,
//                             FileFolderProperties<? extends FileSubfolder> fileFolderProperties) {
//        assertFileExtensionIsSafe(getExtension(file));
//        assertFileContentIsValid(file);
//
//        String folderName = fileFolderProperties.getFolder().getName();
//        String subfolderPath = fileFolderProperties.getSubfolder().getSubfolderPath();
//
//        FileUploadPayload fileUploadPayload = FileUploadPayload.builder()
//                .inputStream(getFileInputStream(file))
//                .fileName(customFilename)
//                .folderPath(FolderPath.builder()
//                        .subfolderPath(subfolderPath)
//                        .path(folderName)
//                        .build())
//                .build();
//
//        return fileUploadAdapter.saveFile(fileUploadPayload);
//    }
//
//    protected String getPathByFilename(String filename,
//                                   FileFolderProperties<? extends FileSubfolder> fileFolderProperties) {
//        if(StringUtils.isEmpty(filename)) {
//            throw new FileException("Cannot retrieve empty file");
//        }
//        return fileUploadAdapter.getPathByFilename(filename, fileFolderProperties);
//    }
//
//    public boolean existsByFilename(String filename,
//                                    FileFolderProperties<? extends FileSubfolder> fileFolderProperties) {
//        return fileUploadAdapter.existsByFilename(filename, fileFolderProperties);
//    }
//
//    protected String getExtension(MultipartFile file) {
//        String originalFilename = file.getOriginalFilename();
//        if (StringUtils.isEmpty(originalFilename) || !StringUtils.contains(originalFilename, FILE_EXTENSION_SEPARATOR)) {
//            throw new FileException("Invalid file name: no extension found.");
//        }
//
//        int fileExtensionIndex = originalFilename.lastIndexOf(FILE_EXTENSION_SEPARATOR) + 1;
//        return originalFilename.substring(fileExtensionIndex);
//    }
//
//    protected void assertFileHasAllowedExtension(MultipartFile file, Set<String> ALLOWED_FILE_EXTENSIONS) {
//        String fileExtension = getExtension(file);
//        String allowedExtensionsList = String.join(", ", ALLOWED_FILE_EXTENSIONS);
//
//        boolean hasForbiddenFileExtension = ALLOWED_FILE_EXTENSIONS.stream()
//                .noneMatch(ext -> StringUtils.equals(ext, fileExtension));
//        if (hasForbiddenFileExtension) {
//            throw new FileException(String.format(
//                    "File extension %s is not allowed. Allowed extensions are: %s", fileExtension, allowedExtensionsList
//            ));
//        }
//    }
//
//    protected String generateRandomUUIDFilename(MultipartFile file) {
//        String extension = getExtension(file);
//        assertFileExtensionIsSafe(extension);
//        return UUID.randomUUID() + FILE_EXTENSION_SEPARATOR + extension;
//    }
//
//    protected void assertFileSizeNotExceeds(MultipartFile file, final long MAX_FILE_SIZE_IN_KILOBYTES) {
//        long fileSizeInBytes = file.getSize() / (1024 ^ 3);
//        if (fileSizeInBytes > MAX_FILE_SIZE_IN_KILOBYTES) {
//            throw new FileException("File is too large");
//        }
//    }
//
//    private InputStream getFileInputStream(MultipartFile file) {
//        try {
//            return file.getInputStream();
//        } catch (IOException e) {
//            throw new FileException("Could not get input stream, because file is corrupted");
//        }
//    }
//
//    private void assertFileExtensionIsSafe(String extension) {
//        if (!extension.matches(ALLOWED_FILENAME_CHARACTERS_PATTERN)
//                || StringUtils.contains(extension, PATH_TRAVERSAL_TOKEN)) {
//            throw new FileException("Invalid file extension.");
//        }
//    }
//
//    private void assertFileContentIsValid(MultipartFile file) {
//        boolean fileIsPresent;
//        try {
//            fileIsPresent = ObjectUtils.isNotEmpty(file) && ArrayUtils.getLength(file.getBytes()) != 0;
//        } catch (IOException ignored) {
//            throw new FileException("Error while reading file: " + file.getOriginalFilename());
//        }
//        if (!fileIsPresent) {
//            throw new FileException("Invalid file content of file: " + file.getOriginalFilename());
//        }
//    }
//
//}
