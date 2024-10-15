package com.hana.hana_spring.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("hash")
public class HashUtil {
    private String salt;
    private String algorithm;

    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            String salt_input = salt + input;
            byte[] hash = digest.digest(salt_input.getBytes());
            return bytes_to_hex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法异常！", e);
        }
    }

    private String bytes_to_hex(byte[] bytes) {
        StringBuilder hex_str = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hex_str.append('0');
            hex_str.append(hex);
        }
        return hex_str.toString();
    }
}
