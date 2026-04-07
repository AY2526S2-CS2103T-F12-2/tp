package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for levenshteinDistance --------------------------------------

    @Test
    public void levenshteinDistance_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.levenshteinDistance(null, "abc"));
        assertThrows(NullPointerException.class, () -> StringUtil.levenshteinDistance("abc", null));
    }

    @Test
    public void levenshteinDistance_identicalStrings_returnsZero() {
        assertEquals(0, StringUtil.levenshteinDistance("", ""));
        assertEquals(0, StringUtil.levenshteinDistance("abc", "abc"));
        assertEquals(0, StringUtil.levenshteinDistance("ABC", "abc")); // case-insensitive
    }

    @Test
    public void levenshteinDistance_oneEmpty_returnsLengthOfOther() {
        assertEquals(3, StringUtil.levenshteinDistance("abc", ""));
        assertEquals(3, StringUtil.levenshteinDistance("", "abc"));
    }

    @Test
    public void levenshteinDistance_substitution_returnsCorrectCount() {
        assertEquals(1, StringUtil.levenshteinDistance("abc", "axc")); // 1 substitution
        assertEquals(2, StringUtil.levenshteinDistance("abc", "xxc")); // 2 substitutions
    }

    @Test
    public void levenshteinDistance_insertionDeletion_returnsCorrectCount() {
        assertEquals(1, StringUtil.levenshteinDistance("abc", "ab")); // 1 deletion
        assertEquals(1, StringUtil.levenshteinDistance("ab", "abc")); // 1 insertion
        assertEquals(2, StringUtil.levenshteinDistance("abc", "a")); // 2 deletions
    }

    //---------------- Tests for fuzzyMatchesWord --------------------------------------

    @Test
    public void fuzzyMatchesWord_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.fuzzyMatchesWord(null, "abc", 2));
        assertThrows(NullPointerException.class, () -> StringUtil.fuzzyMatchesWord("abc", null, 2));
    }

    @Test
    public void fuzzyMatchesWord_emptyKeyword_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.fuzzyMatchesWord("abc", "  ", 2));
    }

    @Test
    public void fuzzyMatchesWord_emptyText_returnsFalse() {
        assertFalse(StringUtil.fuzzyMatchesWord("", "abc", 2));
        assertFalse(StringUtil.fuzzyMatchesWord("   ", "abc", 2));
    }

    @Test
    public void fuzzyMatchesWord_exactMatch_returnsTrue() {
        assertTrue(StringUtil.fuzzyMatchesWord("Alice", "Alice", 2));
        assertTrue(StringUtil.fuzzyMatchesWord("Alice Bob", "bob", 2)); // case-insensitive
    }

    @Test
    public void fuzzyMatchesWord_withinDistance_returnsTrue() {
        // 1 typo in "Alce" vs "Alice" -> distance 1
        assertTrue(StringUtil.fuzzyMatchesWord("Alce", "Alice", 2));
        // 2 typos in "Alce Bob" keyword "Alc" -> "Alce" has distance 1
        assertTrue(StringUtil.fuzzyMatchesWord("Alice Bob", "Alce", 2));
        // keyword "Benson" vs text token "Bnson" -> distance 1
        assertTrue(StringUtil.fuzzyMatchesWord("Bnson Meier", "Benson", 2));
    }

    @Test
    public void fuzzyMatchesWord_beyondDistance_returnsFalse() {
        // "xyz" vs "Alice" -> large distance
        assertFalse(StringUtil.fuzzyMatchesWord("Alice", "xyz", 2));
        // "Alicexx" vs "Alice" -> distance 2, should match
        assertTrue(StringUtil.fuzzyMatchesWord("Alicexx", "Alice", 2));
        // "Alicexxx" vs "Alice" -> distance 3, beyond max
        assertFalse(StringUtil.fuzzyMatchesWord("Alicexxx", "Alice", 2));
    }

    @Test
    public void fuzzyMatchesWord_multiWordKeyword_allPartsMustMatch() {
        // "Alex Yeaa" -> parts ["Alex","Yeaa"]; text "Alex Yeoh" -> tokens ["Alex","Yeoh"]
        // "Alex" matches "Alex" (dist 0), "Yeaa" matches "Yeoh" (dist 2) -> true
        assertTrue(StringUtil.fuzzyMatchesWord("Alex Yeoh", "Alex Yeaa", 2));

        // "Alex Zzzzz" -> "Zzzzz" vs any token in "Alex Yeoh" exceeds distance 2 -> false
        assertFalse(StringUtil.fuzzyMatchesWord("Alex Yeoh", "Alex Zzzzz", 2));

        // Single-part keyword still works as before
        assertTrue(StringUtil.fuzzyMatchesWord("Alex Yeoh", "Yeaa", 2));
    }

    @Test
    public void minFuzzyDistance_multiWordKeyword_returnsMaxPartDist() {
        // "Alex Yeaa" vs "Alex Yeoh": "Alex"->dist 0, "Yeaa"->"Yeoh" dist 2 -> max = 2
        assertEquals(2, StringUtil.minFuzzyDistance("Alex Yeoh", "Alex Yeaa"));
        // exact multi-word substring -> 0
        assertEquals(0, StringUtil.minFuzzyDistance("Alex Yeoh", "Alex Yeoh"));
    }

    //---------------- Tests for minFuzzyDistance --------------------------------------

    @Test
    public void minFuzzyDistance_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.minFuzzyDistance(null, "abc"));
        assertThrows(NullPointerException.class, () -> StringUtil.minFuzzyDistance("abc", null));
    }

    @Test
    public void minFuzzyDistance_substringMatch_returnsZero() {
        // exact substring => distance 0
        assertEquals(0, StringUtil.minFuzzyDistance("Alice Bob", "Alice"));
        assertEquals(0, StringUtil.minFuzzyDistance("wall street", "wall"));
        assertEquals(0, StringUtil.minFuzzyDistance("94351253", "9435")); // phone prefix
        assertEquals(0, StringUtil.minFuzzyDistance("Werner", "ern")); // partial word
    }

    @Test
    public void minFuzzyDistance_exactWordMatch_returnsZero() {
        // exact word (which is also exact substring) => 0
        assertEquals(0, StringUtil.minFuzzyDistance("Alice Bob", "Bob"));
    }

    @Test
    public void minFuzzyDistance_fuzzyTokenMatch_returnsDistance() {
        // "Alce" vs "Alice" => distance 1
        assertEquals(1, StringUtil.minFuzzyDistance("Alce Bob", "Alice"));
        // "Kunz" vs "Kurz" => distance 1
        assertEquals(1, StringUtil.minFuzzyDistance("Carl Kurz", "Kunz"));
    }

    @Test
    public void minFuzzyDistance_noMatch_returnsLargeValue() {
        // "xyz" very different from "Alice" or "Bob"
        int dist = StringUtil.minFuzzyDistance("Alice Bob", "xyz");
        assertTrue(dist > 2);
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

}
