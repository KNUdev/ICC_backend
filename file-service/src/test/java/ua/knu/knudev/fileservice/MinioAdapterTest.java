//package ua.knu.knudev.fileservice;
//
//import io.minio.BucketExistsArgs;
//import io.minio.MakeBucketArgs;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ua.knu.knudev.fileservice.adapter.minio.MinioAdapter;
//import ua.knu.knudev.fileserviceapi.dto.FileUploadPayload;
//import ua.knu.knudev.fileserviceapi.dto.FolderPath;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class MinioAdapterTest {
//    private final String BUCKET_NAME = "test-folder";
//    private final String SUBFOLDER_PATH = "test-subfolder";
//    private final String RANDOM_FILENAME = UUID.randomUUID() + ".pdf";
//
//    @Mock
//    private MinioClient minioClient;
//
//    @InjectMocks
//    private MinioAdapter minioAdapter;
//    private FileUploadPayload fileUploadPayload;
//
//    @BeforeEach
//    void setUp() {
//        InputStream inputStream = new ByteArrayInputStream("dummy content".getBytes());
//        FolderPath folderPath = FolderPath.builder()
//                .path(BUCKET_NAME)
//                .subfolderPath(SUBFOLDER_PATH)
//                .build();
//
//        fileUploadPayload = FileUploadPayload.builder()
//                .inputStream(inputStream)
//                .fileName(RANDOM_FILENAME)
//                .folderPath(folderPath)
//                .build();
//    }
//
//    @Test
//    @DisplayName("Should create the bucket if it does not exist when saving a file")
//    void should_CreateBucketIfNotExists_When_SavingFile() throws Exception {
//        when(minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build()))
//                .thenReturn(false);
//
//        saveFile();
//
//        verify(minioClient).makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
//    }
//
//    @Test
//    @DisplayName("Should not create the bucket if it already exists when saving a file")
//    void should_NotCreateBucket_When_BucketAlreadyExists() throws Exception {
//        when(minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build()))
//                .thenReturn(true);
//
//        saveFile();
//
//        verify(minioClient, times(0)).makeBucket(any(MakeBucketArgs.class));
//
//        verify(minioClient).putObject(any(PutObjectArgs.class));
//    }
//
//    @Test
//    @DisplayName("Should save the file with correct bucket name and object path when saving a file")
//    void should_SaveFileWithCorrectParameters_When_FileIsSaved() throws Exception {
//        when(minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build()))
//                .thenReturn(true);
//
//        saveFile();
//
//        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);
//        verify(minioClient).putObject(captor.capture());
//        PutObjectArgs actualArgs = captor.getValue();
//
//        assertEquals(BUCKET_NAME, actualArgs.bucket(), "Bucket name should be correct.");
//        assertEquals(SUBFOLDER_PATH + "/" + RANDOM_FILENAME, actualArgs.object(),
//                "Object path should be correct."
//        );
//    }
//
//    private void saveFile() {
//        minioAdapter.saveFile(fileUploadPayload);
//    }
//}
