package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TimeSlot;
import seedu.address.model.meeting.Date;
import seedu.address.model.meeting.Meeting;

public class JsonAdaptedMeetingTest {

    @Test
    public void toModelType_validDate_success() throws Exception {
        Meeting sourceMeeting = new Meeting(2, "Project sync", new Date("2026-04-07"),
                new TimeSlot(LocalTime.parse("10:00"), LocalTime.parse("11:00")),
                List.of(ALICE));

        JsonAdaptedMeeting jsonAdaptedMeeting = new JsonAdaptedMeeting(sourceMeeting);
        Meeting modelMeeting = jsonAdaptedMeeting.toModelType();

        assertEquals(sourceMeeting, modelMeeting);
    }

    @Test
    public void toModelType_missingDate_defaultsToToday() throws Exception {
        Date expectedDate = Date.today();
        JsonAdaptedMeeting jsonAdaptedMeeting = new JsonAdaptedMeeting(0, "Project sync", null,
                "10:00", "11:00", List.of(new JsonAdaptedPerson(ALICE)));

        Meeting modelMeeting = jsonAdaptedMeeting.toModelType();

        assertEquals(expectedDate, modelMeeting.getDate());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedMeeting jsonAdaptedMeeting = new JsonAdaptedMeeting(0, "Project sync", "2026-99-99",
                "10:00", "11:00", List.of(new JsonAdaptedPerson(ALICE)));

        assertThrows(IllegalValueException.class, Date.MESSAGE_CONSTRAINTS, jsonAdaptedMeeting::toModelType);
    }
}

