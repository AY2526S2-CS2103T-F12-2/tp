package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Tests for {@link NameOrGroupContainsKeywordsPredicate}.
 */
public class NameOrGroupContainsKeywordsPredicateTest {

    /**
     * Ensures predicate equality follows keywords and group keyword.
     */
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameOrGroupContainsKeywordsPredicate firstPredicate =
                new NameOrGroupContainsKeywordsPredicate(firstPredicateKeywordList, Optional.empty());
        NameOrGroupContainsKeywordsPredicate secondPredicate =
                new NameOrGroupContainsKeywordsPredicate(secondPredicateKeywordList, Optional.of("CS"));

        assertTrue(firstPredicate.equals(firstPredicate));

        NameOrGroupContainsKeywordsPredicate firstPredicateCopy =
                new NameOrGroupContainsKeywordsPredicate(firstPredicateKeywordList, Optional.empty());
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
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(Collections.singletonList("Alice"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    /**
     * Ensures group prefix matching returns true when a group matches.
     */
    @Test
    public void test_groupPrefixMatches_returnsTrue() {
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(Collections.emptyList(), Optional.of("CS"));
        assertTrue(predicate.test(new PersonBuilder().withGroups("CS2103T").build()));
    }

    /**
     * Ensures non-matching group prefixes return false.
     */
    @Test
    public void test_groupPrefixDoesNotMatch_returnsFalse() {
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(Collections.emptyList(), Optional.of("CS"));
        assertFalse(predicate.test(new PersonBuilder().withGroups("MA1521").build()));
    }

    /**
     * Ensures the predicate matches when either name or group matches.
     */
    @Test
    public void test_nameOrGroupMatches_returnsTrue() {
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(Collections.singletonList("Alice"), Optional.of("CS"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withGroups("MA1521").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withGroups("CS2103T").build()));
    }

    /**
     * Ensures the predicate returns false when neither name nor group match.
     */
    @Test
    public void test_noMatch_returnsFalse() {
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(Collections.singletonList("Alice"), Optional.of("CS"));
        assertFalse(predicate.test(new PersonBuilder().withName("Bob").withGroups("MA1521").build()));
    }

    /**
     * Ensures the toString method formats with keywords and group keyword.
     */
    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameOrGroupContainsKeywordsPredicate predicate =
                new NameOrGroupContainsKeywordsPredicate(keywords, Optional.of("CS"));

        String expected = NameOrGroupContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + keywords + ", groupKeyword=" + Optional.of("CS") + "}";
        assertEquals(expected, predicate.toString());
    }
}
