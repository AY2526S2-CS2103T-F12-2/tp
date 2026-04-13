package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Locale;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /** Maximum Levenshtein distance allowed for a fuzzy match. */
    public static final int FUZZY_MATCH_MAX_DISTANCE = 1;

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Computes the Levenshtein distance between two strings (case-insensitive).
     * The Levenshtein distance is the minimum number of single-character edits
     * (insertions, deletions, or substitutions) required to change one string into the other.
     *
     * @param s1 the first string, cannot be null
     * @param s2 the second string, cannot be null
     * @return the Levenshtein distance between {@code s1} and {@code s2}
     */
    public static int levenshteinDistance(String s1, String s2) {
        requireNonNull(s1);
        requireNonNull(s2);
        String a = s1.toLowerCase(Locale.ROOT);
        String b = s2.toLowerCase(Locale.ROOT);
        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    /**
     * Returns true if every whitespace-delimited part of {@code keyword} fuzzy-matches
     * at least one whitespace-delimited token in {@code text} within {@code maxDistance}
     * Levenshtein distance (case-insensitive).
     *
     * <p>For a single-word keyword this is equivalent to checking whether any token in
     * {@code text} is within {@code maxDistance} edits of the keyword.
     * For a multi-word keyword (e.g. {@code "Alex Yeaa"}), every part must independently
     * fuzzy-match some token in {@code text}.</p>
     *
     * @param text the text to search in, cannot be null
     * @param keyword the keyword to search for, cannot be null or empty; may contain spaces
     * @param maxDistance the maximum allowed Levenshtein distance per keyword part (inclusive)
     * @return true if every keyword part finds a match within {@code maxDistance}
     */
    public static boolean fuzzyMatchesWord(String text, String keyword, int maxDistance) {
        requireNonNull(text);
        requireNonNull(keyword);
        String trimmedKeyword = keyword.trim();
        checkArgument(!trimmedKeyword.isEmpty(), "Keyword parameter cannot be empty");
        if (text.isBlank()) {
            return false;
        }
        String[] textTokens = text.split("\\s+");
        String[] keywordParts = trimmedKeyword.split("\\s+");
        for (String part : keywordParts) {
            boolean partMatched = Arrays.stream(textTokens)
                    .anyMatch(token -> levenshteinDistance(token, part) <= maxDistance);
            if (!partMatched) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the minimum fuzzy distance between {@code keyword} and {@code text}.
     * Returns 0 if {@code text} contains {@code keyword} as a case-insensitive substring.
     * Otherwise, splits both the text and the keyword by whitespace and returns the
     * maximum, over all keyword parts, of the minimum Levenshtein distance from that
     * part to any token in {@code text}. This reflects how hard the hardest keyword part
     * is to match, making it suitable for ranking search results.
     *
     * @param text the text to search in, cannot be null
     * @param keyword the keyword to match, cannot be null; may contain spaces
     * @return the minimum distance; 0 for an exact substring match
     */
    public static int minFuzzyDistance(String text, String keyword) {
        requireNonNull(text);
        requireNonNull(keyword);
        String trimmedKeyword = keyword.trim();
        if (text.toLowerCase(Locale.ROOT).contains(trimmedKeyword.toLowerCase(Locale.ROOT))) {
            return 0;
        }
        if (text.isBlank() || trimmedKeyword.isEmpty()) {
            return trimmedKeyword.length();
        }
        String[] textTokens = text.split("\\s+");
        String[] keywordParts = trimmedKeyword.split("\\s+");
        int maxPartDist = 0;
        for (String part : keywordParts) {
            int minTokenDist = Arrays.stream(textTokens)
                    .mapToInt(token -> levenshteinDistance(token, part))
                    .min()
                    .orElse(part.length());
            maxPartDist = Math.max(maxPartDist, minTokenDist);
        }
        return maxPartDist;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
