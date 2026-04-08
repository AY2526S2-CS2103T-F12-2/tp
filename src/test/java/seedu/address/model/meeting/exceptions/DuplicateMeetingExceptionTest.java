package seedu.address.model.meeting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DuplicateMeetingExceptionTest {

    @Test
    public void constructor_defaultMessage() {
        DuplicateMeetingException exception = new DuplicateMeetingException();
        assertEquals("Operation would result in duplicate meetings", exception.getMessage());
    }
}

