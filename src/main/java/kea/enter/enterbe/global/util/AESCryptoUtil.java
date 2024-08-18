package kea.enter.enterbe.global.util;

import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class AESCryptoUtil {
    @Value("${aes.key}")
    private String key;
    @Value("${aes.algorithm}")
    private String algorithm;
    @Value("${aes.iv}")
    private String iv;

    // 암호화
    public String encrypt_AES(String plainText){
        try {
            String result;

            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            // 암호화 적용 및 설정
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 암호화 실행
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(encrypted); // 암호화 인코딩 후 저장

            return result;

        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 복호화
    public String decrypt_AES(String encodedText){
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            // 암호화 적용 및 설정
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 복호화 실행
            byte[] decodeBytes = Base64.getDecoder().decode(encodedText);
            byte[] decrypted = cipher.doFinal(decodeBytes);

            return new String(decrypted, StandardCharsets.UTF_8);

        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
