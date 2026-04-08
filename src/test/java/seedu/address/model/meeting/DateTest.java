package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DateTest {

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Date((String) null));
    }

    @Test
    public void constructor_invalidDateString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Date.MESSAGE_CONSTRAINTS, () -> new Date("2026-13-01"));
    }

    @Test
    public void constructor_nullLocalDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Date((LocalDate) null));
    }

    @Test
    public void isValidDate() {
        assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        assertFalse(Date.isValidDate("2026-13-01"));
        assertFalse(Date.isValidDate("2026/04/01"));
        assertFalse(Date.isValidDate("01-04-2026"));

        assertTrue(Date.isValidDate("2026-04-01"));
    }

    @Test
    public void today_returnsCurrentDate() {
        assertEquals(LocalDate.now(), Date.today().toLocalDate());
    }

    @Test
    public void equalsAndHashCode() {
        Date first = new Date("2026-04-01");
        Date firstCopy = new Date(LocalDate.of(2026, 4, 1));
        Date second = new Date("2026-04-02");

        assertEquals(first, firstCopy);
        assertEquals(first.hashCode(), firstCopy.hashCode());

        assertNotEquals(null, first);
        assertNotEquals("2026-04-01", first);
        assertNotEquals(second, first);
    }

    @Test
    public void toString_returnsIsoDateString() {
        assertEquals("2026-04-01", new Date("2026-04-01").toString());
    }
}
