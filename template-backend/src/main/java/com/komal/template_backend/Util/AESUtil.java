//package com.example.DonourSecu;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//
//public class AESUtil {
//    private static final String ALGORITHM = "AES";
//
//    private static SecretKeySpec getKeySpec() {
//        String key = System.getenv("AESKey");
//        if (key == null || key.isEmpty()) {
//            throw new IllegalStateException("AES_SECRET_KEY not found in environment!");
//        }
//        else {
//            System.out.println("✅ AES Key loaded successfully.");
//        }
//        byte[] decodedKey = Base64.getDecoder().decode(key);
//        return new SecretKeySpec(decodedKey, ALGORITHM);
//    }
//
//    public static String encrypt(String data) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
//        byte[] encrypted = cipher.doFinal(data.getBytes());
//        return Base64.getEncoder().encodeToString(encrypted);
//    }
//
//    public static String decrypt(String encryptedData) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
//        byte[] decoded = Base64.getDecoder().decode(encryptedData);
//        return new String(cipher.doFinal(decoded));
//    }
//}
package com.komal.template_backend.Util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // Load key from environment variable
    private static SecretKeySpec getKeySpec() {
        String key = System.getenv("AESKey"); // must be 32 bytes for AES-256
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("AESKey not found in environment!");
        } else {
            System.out.println("✅ AES Key loaded successfully.");
        }
        byte[] decodedKey = Base64.getDecoder().decode(key);
        if (decodedKey.length != 32) {
            throw new IllegalStateException("AESKey must be 32 bytes for AES-256!");
        }
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    // Encrypt with random IV per record
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[16]; // 16 bytes for AES block
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(), ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Prepend IV to encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt with IV extracted from data
    public static String decrypt(String encryptedData) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedData);
        byte[] iv = Arrays.copyOfRange(combined, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(combined, 16, combined.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
