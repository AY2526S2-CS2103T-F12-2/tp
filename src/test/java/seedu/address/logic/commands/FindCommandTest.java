package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private PersonMatchesKeywordsPredicate createPredicate(
            List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
            List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
            List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
            List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
            List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
            List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
            List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
            List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
            List<String> compulsoryTimeSlotKeywords, List<String> optionalTimeSlotKeywords) {
        return new PersonMatchesKeywordsPredicate(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryTimeSlotKeywords, optionalTimeSlotKeywords);
    }

    /**
     * Ensures find command equality uses predicate equality.
     */
    @Test
    public void equals() {
        PersonMatchesKeywordsPredicate firstPredicate =
                createPredicate(Collections.emptyList(), Collections.singletonList("first"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        PersonMatchesKeywordsPredicate secondPredicate =
                createPredicate(Collections.emptyList(), Collections.singletonList("second"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    /**
     * A single keyword that appears in multiple persons' names returns all of them,
     * including fuzzy matches (ELLE's "Meyer" is distance 1 from "Meier").
     */
    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        // "Meier" exactly matches BENSON and DANIEL; fuzzy-matches ELLE's "Meyer" (dist 1).
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonMatchesKeywordsPredicate predicate = preparePredicate("Meier");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // BENSON and DANIEL have distance 0 (exact); ELLE has distance 1 — sorted accordingly.
        assertEquals(Arrays.asList(BENSON, DANIEL, ELLE), model.getDisplayedPersonList());
    }

    /**
     * Optional (-o) multi-word keyword: any space-separated part matching is sufficient.
     * Mirrors the bug scenario: {@code find -o n/Alice OOO} should match ALICE because
     * "Alice" matches even though "OOO" does not.
     */
    @Test
    public void execute_optionalMultiwordKeyword_partialPartMatches() {
        // Single keyword "Alice OOO" — "Alice" matches ALICE; "OOO" matches nobody.
        // With -o any-match semantics, ALICE should still be found.
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.singletonList("Alice OOO"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getDisplayedPersonList());
    }

    /**
     * Compulsory (-c) keywords use all-match (AND) semantics — only persons whose name
     * contains ALL keywords are returned.
     */
    @Test
    public void execute_andSemanticsKeywords_singlePersonFound() {
        // Compulsory "Carl" AND "Kurz" must both be in the name — only CARL matches.
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Arrays.asList("Carl", "Kurz"), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(CARL), model.getDisplayedPersonList());
    }

    /**
     * Ensures group prefix matching returns matching persons.
     */
    @Test
    public void execute_groupKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("best"),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getDisplayedPersonList());
    }

    /**
     * Ensures address keyword matching returns matching persons.
     */
    @Test
    public void execute_addressKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("wall"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(CARL), model.getDisplayedPersonList());
    }

    /**
     * Ensures major keyword matching returns matching persons.
     */
    @Test
    public void execute_majorKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("Biology"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getDisplayedPersonList());
    }

    /**
     * Ensures the toString method includes the predicate.
     */
    @Test
    public void toStringMethod() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Arrays.asList("keyword"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    @Test
    public void execute_optionalNameKeyword_caseInsensitive() {
        // "mEiEr" case-insensitively matches BENSON and DANIEL exactly, and ELLE's "Meyer" fuzzily (dist 1).
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonMatchesKeywordsPredicate predicate = preparePredicate("mEiEr");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL, ELLE), model.getDisplayedPersonList());
    }

    @Test
    public void execute_optionalMultipleFieldsOrLogic_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("wall"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("Biology"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // CARL has address "wall street" (exact match, distance 0); BENSON matched via major only (distance > 0)
        assertEquals(Arrays.asList(CARL, BENSON), model.getDisplayedPersonList());
    }

    @Test
    public void execute_optionalKeywordNoMatch_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.singletonList("zzzzzz"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_compulsoryNameKeyword_singlePersonsFound() {
        // "Kunz" also fuzzy-matches "Kurz" (Carl) with Levenshtein distance 1
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.singletonList("Kunz"), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // FIONA "Fiona Kunz" exact match (distance 0) comes before CARL "Carl Kurz" (distance 1)
        assertEquals(Arrays.asList(FIONA, CARL), model.getDisplayedPersonList());
    }

    @Test
    public void execute_compulsoryAndOptionalKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.singletonList("Kunz"), Collections.singletonList("Elle"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_compulsoryMatchesButOptionalFails_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.singletonList("Kunz"), Collections.singletonList("zzzzzz"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_multipleCompulsoryFieldsAndLogic_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.singletonList("wall"), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(CARL), model.getDisplayedPersonList());
    }

    @Test
    public void execute_compulsoryAndOptionalDifferentFields_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.singletonList("carL"),
                        Collections.singletonList("wall"), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(CARL), model.getDisplayedPersonList());
    }

    @Test
    public void execute_optionalGroupKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("BeSt"),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getDisplayedPersonList());
    }

    @Test
    public void execute_optionalEmailKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("wern"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ELLE), model.getDisplayedPersonList());
    }

    @Test
    public void execute_allKeywordListsEmpty_allPersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, getTypicalPersons().size());
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(getTypicalPersons(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        PersonMatchesKeywordsPredicate predicate = preparePredicate("Alice");
        FindCommand command = new FindCommand(predicate);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    /**
     * Ensures fuzzy search results are sorted by ascending Levenshtein distance.
     */
    @Test
    public void execute_fuzzySearch_sortedByDistance() {
        // "Kunz" exactly matches FIONA "Fiona Kunz" (distance 0)
        // and fuzzily matches CARL "Carl Kurz" (distance 1, r->n substitution)
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), Collections.singletonList("Kunz"),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        command.execute(model);
        // closest match first: FIONA (distance 0) before CARL (distance 1)
        assertEquals(Arrays.asList(FIONA, CARL), model.getDisplayedPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code PersonMatchesKeywordsPredicate}.
     */
    private PersonMatchesKeywordsPredicate preparePredicate(String userInput) {
        return createPredicate(Collections.emptyList(), Arrays.asList(userInput.split("\\s+")),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }
}
