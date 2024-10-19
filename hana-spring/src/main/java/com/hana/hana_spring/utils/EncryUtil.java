package com.hana.hana_spring.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
public class EncryUtil {
    @Value("${encry.salt}")
    private String salt;
    @Value("${encry.algorithm}")
    private String algorithm;
    @Value("${encry.sym-algorithm}")
    private String sym_algorithm;
    @Value("${encry.key}")
    private String key;

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

    /**
     * 对称加密
     */
    public String encrypt(String data) throws Exception {
        if (data == null) {
            return null;
        }
        SecretKeySpec key_spec = new SecretKeySpec(pad_key(key).getBytes(), sym_algorithm);
        Cipher cipher = Cipher.getInstance(sym_algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key_spec);
        byte[] encrypted_bytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted_bytes);
    }

    /**
     * 对称解密
     */
    public String decrypt(String encrypted_data) throws Exception {
        if (encrypted_data == null) {
            return null;
        }
        SecretKeySpec key_spec = new SecretKeySpec(pad_key(key).getBytes(), sym_algorithm);
        Cipher cipher = Cipher.getInstance(sym_algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key_spec);
        byte[] decoded_bytes = Base64.getDecoder().decode(encrypted_data);
        byte[] decrypted_bytes = cipher.doFinal(decoded_bytes);
        return new String(decrypted_bytes);
    }

    private String pad_key(String key) {
        int length = 32;
        if (key.length() < length) {
            StringBuilder sb = new StringBuilder(key);
            while (sb.length() < length) {
                sb.append("0"); // 使用 "0" 补齐
            }
            return sb.toString();
        }
        return key.substring(0, length); // 如果超过则截断
    }

}
