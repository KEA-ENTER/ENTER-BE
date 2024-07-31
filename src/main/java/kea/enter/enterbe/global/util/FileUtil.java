package kea.enter.enterbe.global.util;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    private final static String IMAGE = "image";

    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith(IMAGE);
    }

    public boolean isImageFileList(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (!isImageFile(file)) {
                return false;
            }
        }
        return true;
    }
}