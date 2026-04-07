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
     * Ensures predicate equality follows the configured keywords.
     */
    @Test
    public void equals() {
        PersonMatchesKeywordsPredicate firstPredicate =
                createPredicate(List.of("first"), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        PersonMatchesKeywordsPredicate secondPredicate =
                createPredicate(List.of("second"), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("CS"), List.of(), List.of());

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesKeywordsPredicate firstPredicateCopy =
                createPredicate(List.of("first"), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
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
                createPredicate(List.of(), Collections.singletonList("Alice"), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    /**
     * Ensures address keyword matching returns true when an address matches.
     */
    @Test
    public void test_addressContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of("Jurong"),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withAddress("123 Jurong Ave").build()));
    }

    /**
     * Ensures phone keyword matching returns true when a phone matches.
     */
    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of("94351253"),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));
    }

    /**
     * Ensures major keyword matching returns true when a major matches.
     */
    @Test
    public void test_majorContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("CS"), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withMajors("CS").build()));
    }

    /**
     * Ensures email keyword matching returns true when an email matches.
     */
    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of("alice"), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    /**
     * Ensures group prefix matching returns true when a group matches.
     */
    @Test
    public void test_groupContainsKeyword_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("CS"), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withGroups("CS2103T").build()));
    }

    /**
     * Ensures tag keyword matching returns true when a tag matches.
     */
    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of("friends"),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    /**
     * Ensures position keyword matching returns true when a position matches.
     */
    @Test
    public void test_positionContainsKeywords_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("Teaching Assistant"), List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().withPositions("Teaching Assistant").build()));
    }

    /**
     * Ensures no matching fields returns false.
     */
    @Test
    public void test_noMatch_returnsFalse() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of("Alice"), List.of(), List.of("Jurong"),
                        List.of(), List.of("9435"), List.of(), List.of("CS"), List.of(), List.of("alice"),
                        List.of(), List.of("friend"), List.of(), List.of("TA"), List.of(), List.of("CS"),
                        List.of(), List.of());
        assertFalse(predicate.test(new PersonBuilder().withName("Bob").withAddress("Main Street")
                .withPhone("123456").withEmail("bob@example.com").withMajors("Math").withGroups("MA1521").build()));
    }

    @Test
    public void test_allKeywordListsEmpty_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of());
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_optionalTimeKeyword_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of("0900"));
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_exactTimeMatch_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of("1000"));
        assertTrue(predicate.test(new PersonBuilder().withAvailableHours("0900-1200").build()));
    }

    @Test
    public void test_slotWithinAvailability_returnsTrue() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of("1000-1100"));
        assertTrue(predicate.test(new PersonBuilder().withAvailableHours("0900-1200").build()));
    }

    @Test
    public void test_compulsoryTimeKeyword_returnsFalse() {
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of("1300"), List.of());
        assertFalse(predicate.test(new PersonBuilder().withAvailableHours("0900-1200").build()));
    }

    /**
     * Ensures the toString method formats all keyword lists.
     */
    @Test
    public void toStringMethod() {
        List<String> keywords = Arrays.asList("keyword1", "keyword2");
        PersonMatchesKeywordsPredicate predicate =
                createPredicate(keywords, List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of("CS"), List.of(), List.of());

        String expected = PersonMatchesKeywordsPredicate.class.getCanonicalName()
                + "{compulsoryNameKeywords=" + keywords
                + ", optionalNameKeywords=[]"
                + ", compulsoryAddressKeywords=[]"
                + ", optionalAddressKeywords=[]"
                + ", compulsoryPhoneKeywords=[]"
                + ", optionalPhoneKeywords=[]"
                + ", compulsoryMajorKeywords=[]"
                + ", optionalMajorKeywords=[]"
                + ", compulsoryEmailKeywords=[]"
                + ", optionalEmailKeywords=[]"
                + ", compulsoryTagKeywords=[]"
                + ", optionalTagKeywords=[]"
                + ", compulsoryPositionKeywords=[]"
                + ", optionalPositionKeywords=[]"
                + ", compulsoryGroupKeywords=[]"
                + ", optionalGroupKeywords=" + List.of("CS")
                + ", compulsoryTimeSlotKeywords=[]"
                + ", optionalTimeSlotKeywords=[]}";
        assertEquals(expected, predicate.toString());
    }
}
