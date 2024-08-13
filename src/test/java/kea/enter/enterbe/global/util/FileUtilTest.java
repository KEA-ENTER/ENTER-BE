package kea.enter.enterbe.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import kea.enter.enterbe.IntegrationTestSupport;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class FileUtilTest extends IntegrationTestSupport {

    @DisplayName(value="파일이 이미지면 true를 반환한다. (성공)")
    @Test
    public void isImageFile() throws Exception {
        //given
        String writerData = "str1,str2,str3,str4";
        MockMultipartFile file = new MockMultipartFile("image.jpg","image.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), writerData.getBytes(StandardCharsets.UTF_8));
        //when
        Boolean isImage = fileUtil.isImageFile(file);
        //then
        assertThat(isImage).isTrue();
    }
    @DisplayName(value="파일 리스트가 전부 이미지 파일이면 true를 반환한다. (성공)")
    @Test
    public void isImageFileList() throws Exception {
        //given
        String writerData = "str1,str2,str3,str4";
        MockMultipartFile file1 = new MockMultipartFile("image1.jpg","image.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), writerData.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("image2.jpg","image.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), writerData.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file3 = new MockMultipartFile("image3.jpg","image.jpg",
            ContentType.IMAGE_JPEG.getMimeType(), writerData.getBytes(StandardCharsets.UTF_8));
        List<MultipartFile> imageList = List.of(file1,file2,file3);
        //when
        Boolean isImage = fileUtil.isImageFileList(imageList);
        //then
        assertThat(isImage).isTrue();
    }
}