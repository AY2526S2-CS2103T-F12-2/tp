package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                String.format(AddCommand.MESSAGE_DUPLICATE_PERSON, "name, phone, email"));
    }

    @Test
    public void execute_duplicateNameDifferentCase_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        Person sameNameDifferentCase = new PersonBuilder(personInList)
                .withName(personInList.getName().fullName.toUpperCase())
                .withPhone("91112223")
                .withEmail("different@example.com")
                .build();

        assertCommandFailure(new AddCommand(sameNameDifferentCase), model,
                String.format(AddCommand.MESSAGE_DUPLICATE_PERSON, "name"));
    }

    @Test
    public void execute_duplicateEmailDifferentCase_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        Person sameEmailDifferentCase = new PersonBuilder(personInList)
                .withName("Unique Person")
                .withPhone("92223334")
                .withEmail(personInList.getEmail().value.toUpperCase())
                .build();

        assertCommandFailure(new AddCommand(sameEmailDifferentCase), model,
                String.format(AddCommand.MESSAGE_DUPLICATE_PERSON, "email"));
    }

}
