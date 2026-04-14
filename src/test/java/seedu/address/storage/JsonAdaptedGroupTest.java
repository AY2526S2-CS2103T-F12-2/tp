package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Group;

public class JsonAdaptedGroupTest {

    private static final String VALID_GROUP = "CS2103T";
    private static final String INVALID_GROUP = "CS 2103T"; // spaces not allowed in Group

    @Test
    public void toModelType_validGroup_returnsGroup() throws Exception {
        JsonAdaptedGroup adapted = new JsonAdaptedGroup(VALID_GROUP);
        assertEquals(new Group(VALID_GROUP), adapted.toModelType());
    }

    @Test
    public void toModelType_invalidGroup_throwsIllegalValueException() {
        JsonAdaptedGroup adapted = new JsonAdaptedGroup(INVALID_GROUP);
        assertThrows(IllegalValueException.class, Group.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_fromGroupSource_roundTrips() throws Exception {
        Group original = new Group(VALID_GROUP);
        JsonAdaptedGroup adapted = new JsonAdaptedGroup(original);
        assertEquals(original, adapted.toModelType());
    }

    @Test
    public void getGroupName_returnsStoredName() {
        JsonAdaptedGroup adapted = new JsonAdaptedGroup(VALID_GROUP);
        assertEquals(VALID_GROUP, adapted.getGroupName());
    }
}
