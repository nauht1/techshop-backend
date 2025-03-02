package hcmute.techshop.Service.UploadFile;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploadFileService {
    String uploadImageMultipart(MultipartFile imageFile) throws IOException;
    String uploadImageBytes(byte[] imageBytes) throws IOException;
}
