package com.example.elearningplatform.cloudinary;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@SuppressWarnings("rawtypes")
public class CloudinaryService {
    Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dcvrdin4a");
        valuesMap.put("api_key", "412872152882384");
        valuesMap.put("api_secret", "Jv_O6IoGQhF6FwPhUw47xol5fFg");
        cloudinary = new Cloudinary(valuesMap);
    }
/******************************************************************************************************************/

 
    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }

/******************************************************************************************************************/

public Map delete(String id) throws IOException {
    return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
}

private File convert(MultipartFile multipartFile) throws IOException {
    File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    FileOutputStream fo = new FileOutputStream(file);
    fo.write(multipartFile.getBytes());
    fo.close();
    return file;
}
/******************************************************************************************************************/

}
