package kea.enter.enterbe.global.common.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kea.enter.enterbe.global.util.AESCryptoUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private final AESCryptoUtil aesCryptoUtil;

    @Override
    public String convertToDatabaseColumn(String plainText) {
        if(plainText == null) return null;
        return aesCryptoUtil.encrypt_AES(plainText);
    }

    @Override
    public String convertToEntityAttribute(String encryptedText) {
        if(encryptedText == null) return null;
        return aesCryptoUtil.decrypt_AES(encryptedText);
    }
}
