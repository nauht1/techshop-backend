package hcmute.techshop.Service.UploadFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadFileServiceImpl implements IUploadFileService{
    private final Cloudinary cloudinary;

    public UploadFileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImageMultipart(MultipartFile imageFile) throws IOException {
        // Validate image file
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            // Upload file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.emptyMap()
            );

            // Return the secure URL of the uploaded image
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }

    @Override
    public String uploadImageBytes(byte[] imageBytes) throws IOException {
        try {
            // Upload image bytes to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    imageBytes,
                    ObjectUtils.emptyMap()
            );

            // Return the secure URL of the uploaded image
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }
}
