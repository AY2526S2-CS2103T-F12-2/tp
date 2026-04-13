package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortCommandTest {

    private Model model;

    @Test
    public void execute_sortFirstnameAsc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, true);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "firstname", "ascending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortFirstnameDesc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, false);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "firstname", "descending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortLastnameAsc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.LASTNAME, true);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "lastname", "ascending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortLastnameDesc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.LASTNAME, false);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "lastname", "descending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortRecentAsc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.RECENT, true);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "recent", "ascending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortRecentDesc_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        SortCommand sortCommand = new SortCommand(SortField.RECENT, false);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "recent", "descending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortFirstnameTiebreakerAndPinned_success() {
        model = new ModelManager();
        Person p1 = new PersonBuilder().withName("John Beta").withPhone("111111").withEmail("b@b.com").withPinned(true).build();
        Person p2 = new PersonBuilder().withName("John Alpha").withPhone("222222").withEmail("a@a.com").withPinned(true).build();
        Person p3 = new PersonBuilder().withName("Alice Beta").withPhone("333333").withEmail("c@c.com").withPinned(false).build();
        Person p4 = new PersonBuilder().withName("Alice Alpha").withPhone("444444").withEmail("d@d.com").withPinned(false).build();

        model.addPerson(p1);
        model.addPerson(p2);
        model.addPerson(p3);
        model.addPerson(p4);

        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, true);
        sortCommand.execute(model);

        // Expected order:
        // Pinned elements first (sorted by firstname: John Alpha, then John Beta)
        // Unpinned elements next (sorted by firstname: Alice Alpha, then Alice Beta)
        assertEquals(p2, model.getDisplayedPersonList().get(0));
        assertEquals(p1, model.getDisplayedPersonList().get(1));
        assertEquals(p4, model.getDisplayedPersonList().get(2));
        assertEquals(p3, model.getDisplayedPersonList().get(3));
    }

    @Test
    public void equals() {
        SortCommand sortFirstAsc = new SortCommand(SortField.FIRSTNAME, true);
        SortCommand sortFirstDesc = new SortCommand(SortField.FIRSTNAME, false);
        SortCommand sortLastAsc = new SortCommand(SortField.LASTNAME, true);
        SortCommand sortLastDesc = new SortCommand(SortField.LASTNAME, false);

        // same object -> returns true
        assertTrue(sortFirstAsc.equals(sortFirstAsc));

        // same values -> returns true
        SortCommand sortFirstAscCopy = new SortCommand(SortField.FIRSTNAME, true);
        assertTrue(sortFirstAsc.equals(sortFirstAscCopy));

        // different types -> returns false
        assertFalse(sortFirstAsc.equals(1));

        // null -> returns false
        assertFalse(sortFirstAsc.equals(null));

        // different properties -> returns false
        assertFalse(sortFirstAsc.equals(sortFirstDesc));
        assertFalse(sortFirstAsc.equals(sortLastAsc));
        assertFalse(sortFirstAsc.equals(sortLastDesc));
    }

    @Test
    public void toStringMethod() {
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, true);
        String expected = SortCommand.class.getCanonicalName() + "{sortField=FIRSTNAME, isAscending=true}";
        assertEquals(expected, sortCommand.toString());
    }
}
