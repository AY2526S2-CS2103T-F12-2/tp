package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different positions -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPositions("Mentor").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different majors -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withMajors("ComputerScience").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different groups -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withGroups("CS2103").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different available hours -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAvailableHours("0900-1000").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different flags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).build();
        editedAmy.setEditFlag(EditFlag.APPEND);
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void isAnyFieldEdited() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setEditFlag(EditFlag.APPEND);
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        assertTrue(descriptor.isAnyFieldEdited());

        // Empty tag set is used to reset tags, so this should still count as edited.
        descriptor = new EditPersonDescriptorBuilder().withTags().build();
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + ", positions="
                + editPersonDescriptor.getPositions().orElse(null) + ", majors="
                + editPersonDescriptor.getMajors().orElse(null) + ", groups="
                + editPersonDescriptor.getGroups().orElse(null) + ", available hours="
                + editPersonDescriptor.getAvailableHours().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }

    @Test
    public void copyConstructor_withAvailableHours_copiesAvailableHours() {
        EditPersonDescriptor original = new EditPersonDescriptorBuilder()
                .withAvailableHours("0900-1000")
                .build();

        EditPersonDescriptor copy = new EditPersonDescriptor(original);

        assertEquals(original, copy);
        assertTrue(copy.getAvailableHours().isPresent());
        assertEquals(original.getAvailableHours().get(), copy.getAvailableHours().get());
    }
}
