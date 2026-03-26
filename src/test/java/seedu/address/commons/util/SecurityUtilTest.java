package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SecurityUtilTest {

    @Test
    public void hashPassword_nullPassword_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> SecurityUtil.hashPassword(null));
    }

    @Test
    public void hashPassword_samePassword_returnsSameHash() {
        assertEquals(SecurityUtil.hashPassword("password123"), SecurityUtil.hashPassword("password123"));
    }

    @Test
    public void hashPassword_differentPasswords_returnsDifferentHashes() {
        assertNotEquals(SecurityUtil.hashPassword("abc"), SecurityUtil.hashPassword("ABC"));
    }

    @Test
    public void hashPassword_returnsValidSha256HexString() {
        String hash = SecurityUtil.hashPassword("test");
        // SHA-256 always produces 64 hex characters
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]+"));
    }

    @Test
    public void hashPassword_emptyPassword_returnsHash() {
        String hash = SecurityUtil.hashPassword("");
        assertEquals(64, hash.length());
    }

    @Test
    public void verifyPassword_nullStoredHash_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> SecurityUtil.verifyPassword("password", null));
    }

    @Test
    public void verifyPassword_correctPassword_returnsTrue() {
        String hash = SecurityUtil.hashPassword("correctPassword");
        assertTrue(SecurityUtil.verifyPassword("correctPassword", hash));
    }

    @Test
    public void verifyPassword_wrongPassword_returnsFalse() {
        String hash = SecurityUtil.hashPassword("correctPassword");
        assertFalse(SecurityUtil.verifyPassword("wrongPassword", hash));
    }

    @Test
    public void verifyPassword_emptyPassword_matchesEmptyHash() {
        String hash = SecurityUtil.hashPassword("");
        assertTrue(SecurityUtil.verifyPassword("", hash));
        assertFalse(SecurityUtil.verifyPassword("notempty", hash));
    }

    @Test
    public void verifyPassword_caseSensitive() {
        String hash = SecurityUtil.hashPassword("Password");
        assertFalse(SecurityUtil.verifyPassword("password", hash));
        assertFalse(SecurityUtil.verifyPassword("PASSWORD", hash));
    }
}
