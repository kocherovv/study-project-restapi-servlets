package net.example.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    private static final String HASH_ALGORITHM = "SHA-256";

    public static byte[] hashPassword(byte[] password) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            return md.digest(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password.", e);
        }
    }
}
