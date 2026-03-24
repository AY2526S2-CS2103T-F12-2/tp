package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link PersonMatchesKeywordsPredicate}.
 */
public class PersonMatchesKeywordsPredicateTest {

    /**
     * Ensures predicate equality follows the configured keywords.
     */
    @Test
    public void equals() {
        PersonMatchesKeywordsPredicate firstPredicate =
                new PersonMatchesKeywordsPredicate(List.of("first"), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        PersonMatchesKeywordsPredicate secondPredicate =
                new PersonMatchesKeywordsPredicate(List.of("second"), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of("CS"));

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesKeywordsPredicate firstPredicateCopy =
                new PersonMatchesKeywordsPredicate(List.of("first"), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    /**
     * Ensures name keyword matching returns true when a name matches.
     */
    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(Collections.singletonList("Alice"), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    /**
     * Ensures address keyword matching returns true when an address matches.
     */
    @Test
    public void test_addressContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of("Jurong"), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withAddress("123 Jurong Ave").build()));
    }

    /**
     * Ensures phone keyword matching returns true when a phone matches.
     */
    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of("94351253"), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));
    }

    /**
     * Ensures major keyword matching returns true when a major matches.
     */
    @Test
    public void test_majorContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(), List.of("CS"), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withMajors("CS").build()));
    }

    /**
     * Ensures email keyword matching returns true when an email matches.
     */
    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(), List.of(), List.of("alice"),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    /**
     * Ensures group prefix matching returns true when a group matches.
     */
    @Test
    public void test_groupContainsKeyword_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of("CS"), List.of());
        assertTrue(predicate.test(new PersonBuilder().withGroups("CS2103T").build()));
    }

    /**
     * Ensures tag keyword matching returns true when a tag matches.
     */
    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of("friends"), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    /**
     * Ensures position keyword matching returns true when a position matches.
     */
    @Test
    public void test_positionContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("Teaching Assistant"), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withPositions("Teaching Assistant").build()));
    }

    /**
     * Ensures no matching fields returns false.
     */
    @Test
    public void test_noMatch_returnsFalse() {
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(List.of("Alice"), List.of("Jurong"), List.of("9435"),
                        List.of("CS"), List.of("alice"), List.of("friend"), List.of("TA"), List.of("CS"), List.of());
        assertFalse(predicate.test(new PersonBuilder().withName("Bob").withAddress("Main Street")
                .withPhone("123456").withEmail("bob@example.com").withMajors("Math").withGroups("MA1521").build()));
    }

    /**
     * Ensures the toString method formats all keyword lists.
     */
    @Test
    public void toStringMethod() {
        List<String> keywords = Arrays.asList("keyword1", "keyword2");
        PersonMatchesKeywordsPredicate predicate =
                new PersonMatchesKeywordsPredicate(keywords, List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of("CS"), List.of());

        String expected = PersonMatchesKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + keywords + ", addressKeywords=[], phoneKeywords=[], majorKeywords=[]"
                + ", emailKeywords=[], tagKeywords=[], positionKeywords=[], groupKeywords=" + List.of("CS")
                + ", availableHoursKeywords=[]}";
        assertEquals(expected, predicate.toString());
    }
}
