package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for password hashing.
 */
public final class SecurityUtil {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private SecurityUtil() {}

    /**
     * Returns the SHA-256 hash of the given password as a hex string.
     *
     * @param password The plaintext password to hash. Must not be null.
     * @return The hex-encoded SHA-256 hash.
     */
    public static String hashPassword(String password) {
        requireNonNull(password);
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies that the given plaintext password matches the stored hash.
     */
    public static boolean verifyPassword(String password, String storedHash) {
        requireNonNull(storedHash);
        return hashPassword(password).equals(storedHash);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_DIGITS[value >>> 4];
            hexChars[i * 2 + 1] = HEX_DIGITS[value & 0x0F];
        }
        return new String(hexChars);
    }
}
