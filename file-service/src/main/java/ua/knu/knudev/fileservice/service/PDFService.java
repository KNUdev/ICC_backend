//package ua.knu.knudev.fileservice.service;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import ua.knu.knudev.fileservice.adapter.FileUploadAdapter;
//import ua.knu.knudev.fileservice.config.TaskFileConfigProperties;
//import ua.knu.knudev.fileserviceapi.api.PDFServiceApi;
//import ua.knu.knudev.fileserviceapi.folder.FileFolderProperties;
//import ua.knu.knudev.fileserviceapi.folder.PdfFolder;
//import ua.knu.knudev.fileserviceapi.subfolder.PdfSubfolderI;
//import ua.knu.knudev.knudevcommon.exception.FileException;
//
//@Service
//public class PDFService extends FileService implements PDFServiceApi {
//    private final TaskFileConfigProperties taskFileConfigProperties;
//
//    public PDFService(FileUploadAdapter fileUploadAdapter, TaskFileConfigProperties taskFileConfigProperties) {
//        super(fileUploadAdapter);
//        this.taskFileConfigProperties = taskFileConfigProperties;
//    }
//
//    @Override
//    public String uploadFile(MultipartFile file, String customFilename, PdfSubfolderI subfolder) {
//        assertFileSizeNotExceeds(file, taskFileConfigProperties.maximumSizeInKilobytes());
//        assertFileHasAllowedExtension(file, taskFileConfigProperties.allowedExtensions());
//
//        return uploadFile(file, customFilename, getFolderProperties(subfolder));
//    }
//
//    @Override
//    public String uploadFile(MultipartFile file, PdfSubfolderI subfolder) {
//        assertFileSizeNotExceeds(file, taskFileConfigProperties.maximumSizeInKilobytes());
//        assertFileHasAllowedExtension(file, taskFileConfigProperties.allowedExtensions());
//
//        String randomUUIDFilename = generateRandomUUIDFilename(file);
//        return uploadFile(file, randomUUIDFilename, getFolderProperties(subfolder));
//    }
//
//    @Override
//    public boolean existsByFilename(String filename, PdfSubfolderI subfolder) {
//        return existsByFilename(filename, getFolderProperties(subfolder));
//    }
//
//    @Override
//    public String getPathByFilename(String filename, PdfSubfolderI subfolder) {
//        return getPathByFilename(filename, getFolderProperties(subfolder));
//    }
//
//    @Override
//    public void removeByFilename(String filename, PdfSubfolderI subfolder) {
//        FileFolderProperties<PdfSubfolderI> folderProperties = getFolderProperties(subfolder);
//        boolean fileExists = existsByFilename(filename, folderProperties);
//        if(!fileExists) {
//            throw new FileException(String.format("File with filename %s does not exist", filename));
//        }
//
//        fileUploadAdapter.deleteByFilename(filename, folderProperties);
//    }
//
//    @Override
//    public String updateByFilename(String oldFilename, MultipartFile newFile, PdfSubfolderI subfolder) {
//        if(StringUtils.isNotEmpty(oldFilename)) {
//            removeByFilename(oldFilename, subfolder);
//        }
//        return uploadFile(newFile, subfolder);
//    }
//
//    private FileFolderProperties<PdfSubfolderI> getFolderProperties(PdfSubfolderI pdfSubfolder) {
//        return FileFolderProperties.builder(PdfFolder.INSTANCE)
//                .subfolder(pdfSubfolder)
//                .build();
//    }
//
//    @Override
//    public String getFile() {
//        //todo
//        return null;
//    }
//}
