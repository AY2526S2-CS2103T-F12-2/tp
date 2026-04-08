package seedu.address.model.meeting.exceptions;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class MeetingNotFoundExceptionTest {

    @Test
    public void constructor_defaultMessageIsNull() {
        MeetingNotFoundException exception = new MeetingNotFoundException();
        assertNull(exception.getMessage());
    }
}

