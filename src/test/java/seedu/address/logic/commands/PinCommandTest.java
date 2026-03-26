package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class PinCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnpinned_success() {
        Person personToPin = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PinCommand pinCommand = new PinCommand(INDEX_FIRST_PERSON);

        Person pinnedPerson = createPinnedPerson(personToPin);

        String expectedMessage = String.format(PinCommand.MESSAGE_PIN_PERSON_SUCCESS,
                Messages.format(pinnedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToPin, pinnedPerson);

        assertCommandSuccess(pinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexPinned_success() {
        Person personToPin = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person pinnedPerson = createPinnedPerson(personToPin);
        model.setPerson(personToPin, pinnedPerson);

        PinCommand pinCommand = new PinCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(PinCommand.MESSAGE_UNPIN_PERSON_SUCCESS,
                Messages.format(personToPin));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToPin, pinnedPerson);
        expectedModel.setPerson(pinnedPerson, personToPin);

        assertCommandSuccess(pinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PinCommand pinCommand = new PinCommand(outOfBoundIndex);

        assertCommandFailure(pinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_maxPinsReached_throwsCommandException() {
        Person p1 = model.getFilteredPersonList().get(0);
        Person p2 = model.getFilteredPersonList().get(1);
        Person p3 = model.getFilteredPersonList().get(2);

        model.setPerson(p1, createPinnedPerson(p1));
        model.setPerson(p2, createPinnedPerson(p2));
        model.setPerson(p3, createPinnedPerson(p3));

        Index fourthIndex = Index.fromOneBased(4);
        PinCommand pinCommand = new PinCommand(fourthIndex);

        assertCommandFailure(pinCommand, model, PinCommand.MESSAGE_MAX_PINS_REACHED);
    }

    @Test
    public void equals() {
        PinCommand pinFirstCommand = new PinCommand(INDEX_FIRST_PERSON);
        PinCommand pinSecondCommand = new PinCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(pinFirstCommand.equals(pinFirstCommand));

        // same values -> returns true
        PinCommand pinFirstCommandCopy = new PinCommand(INDEX_FIRST_PERSON);
        assertTrue(pinFirstCommand.equals(pinFirstCommandCopy));

        // different types -> returns false
        assertFalse(pinFirstCommand.equals(1));

        // null -> returns false
        assertFalse(pinFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(pinFirstCommand.equals(pinSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        PinCommand pinCommand = new PinCommand(targetIndex);
        String expected = PinCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, pinCommand.toString());
    }

    private static Person createPinnedPerson(Person person) {
        return new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(),
                new HashSet<>(person.getTags()), new HashSet<>(person.getPositions()),
                new HashSet<>(person.getMajors()), new HashSet<>(person.getGroups()),
                new HashSet<>(person.getAvailableHours()), person.getProfilePicturePath(), true);
    }
}
