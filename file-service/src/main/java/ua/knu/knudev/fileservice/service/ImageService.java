package ua.knu.knudev.fileservice.service;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileservice.adapter.FileUploadAdapter;
import ua.knu.knudev.fileservice.config.ImageFileConfigProperties;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.folder.FileFolderProperties;
import ua.knu.knudev.fileserviceapi.folder.ImageFolder;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.icccommon.exception.FileException;

import java.io.IOException;

@Service
public class ImageService extends FileService implements ImageServiceApi {
    private final ImageFileConfigProperties imageFileConfigProperties;

    public ImageService(FileUploadAdapter fileUploadAdapter, ImageFileConfigProperties imageFileConfigProperties) {
        super(fileUploadAdapter);
        this.imageFileConfigProperties = imageFileConfigProperties;
    }

    @Override
    public String uploadFile(MultipartFile file, String customFilename, ImageSubfolder subfolder) {
        checkFileValidity(file);
        return uploadFile(file, customFilename, getFolderProperties(subfolder));
    }

    @Override
    public String uploadFile(MultipartFile file, ImageSubfolder subfolder) {
        checkFileValidity(file);

        String filename = generateRandomUUIDFilename(file);
        return uploadFile(file, filename, getFolderProperties(subfolder));
    }

    @Override
    public boolean existsByFilename(String filename, ImageSubfolder subfolder) {
        return existsByFilename(filename, getFolderProperties(subfolder));
    }

    @Override
    public String getPathByFilename(String filename, ImageSubfolder subfolder) {
        return getPathByFilename(filename, getFolderProperties(subfolder));
    }

    @Override
    public void removeByFilename(String filename, ImageSubfolder subfolder) {
        FileFolderProperties<ImageSubfolder> folderProperties = getFolderProperties(subfolder);
        boolean fileExists = existsByFilename(filename, folderProperties);
        if(!fileExists) {
            throw new FileException(String.format("File with filename %s does not exist", filename));
        }

        fileUploadAdapter.deleteByFilename(filename, folderProperties);
    }

    @Override
    public String updateByFilename(String oldFilename, MultipartFile newFile, ImageSubfolder subfolder) {
        if(StringUtils.isNotEmpty(oldFilename)) {
            removeByFilename(oldFilename, subfolder);
        }
        return uploadFile(newFile, subfolder);
    }

    private void checkFileValidity(MultipartFile file) {
        if(ObjectUtils.isEmpty(file)) {
            throw new FileException("Cannot upload null image");
        }
        assertFileHasAllowedExtension(file, imageFileConfigProperties.allowedExtensions());
        checkFileSize(file);
    }

    private FileFolderProperties<ImageSubfolder> getFolderProperties(ImageSubfolder imageSubfolder) {
        return FileFolderProperties.builder(ImageFolder.INSTANCE)
                .subfolder(imageSubfolder)
                .build();
    }

    //todo perhaps use this method also for pdf service
    private void checkFileSize(MultipartFile file) {
        try {
            int fileSizeInKilobytes = file.getBytes().length / 1024;
            final Integer MAX_SIZE_IN_KILOBYTES = imageFileConfigProperties.maximumSizeInKilobytes();

            if (fileSizeInKilobytes > MAX_SIZE_IN_KILOBYTES) {
                throw new FileException("File size is too big. Maximum allowed size is 2 megabytes");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
