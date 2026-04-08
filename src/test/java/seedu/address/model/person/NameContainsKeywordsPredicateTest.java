package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link NameContainsKeywordsPredicate}.
 */
public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        assertTrue(firstPredicate.equals(firstPredicate));

        NameContainsKeywordsPredicate firstPredicateCopy =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(List.of("Alice", "Bob"));
        assertFalse(predicate.test(new PersonBuilder().withName("Carol Dan").build()));
    }

    @Test
    public void test_caseInsensitiveMatch_returnsTrue() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("aLiCe"));
        assertTrue(predicate.test(new PersonBuilder().withName("ALICE Bob").build()));
    }

    @Test
    public void test_partialWordDoesNotMatch_returnsFalse() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Ali"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_emptyKeywordList_returnsFalse() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_blankKeyword_throwsIllegalArgumentException() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("   "));
        assertThrows(IllegalArgumentException.class,
                () -> predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("foo", "bar");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

