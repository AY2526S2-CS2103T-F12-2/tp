package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given or any of the person's
 * {@code Group} values matches the provided group keyword (prefix, case-insensitive).
 */
public class NameOrGroupContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final Optional<String> groupKeyword;

    /**
     * Creates a predicate that matches on name keywords and/or a group keyword.
     */
    public NameOrGroupContainsKeywordsPredicate(List<String> nameKeywords, Optional<String> groupKeyword) {
        this.nameKeywords = nameKeywords;
        this.groupKeyword = groupKeyword;
    }

    /**
     * Returns true if the person matches any name keyword or the group keyword.
     */
    @Override
    public boolean test(Person person) {
        boolean nameMatches = !nameKeywords.isEmpty()
                && nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
        boolean groupMatches = groupKeyword
                .map(keyword -> hasGroupWithPrefixIgnoreCase(person, keyword))
                .orElse(false);
        return nameMatches || groupMatches;
    }

    /**
     * Returns true if the person has at least one group value that starts with the keyword (case-insensitive).
     */
    private boolean hasGroupWithPrefixIgnoreCase(Person person, String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return person.getGroups().stream()
                .anyMatch(group -> group.value.toLowerCase(Locale.ROOT).startsWith(normalizedKeyword));
    }

    /**
     * Returns true if both predicates have the same name and group keyword settings.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NameOrGroupContainsKeywordsPredicate)) {
            return false;
        }

        NameOrGroupContainsKeywordsPredicate otherPredicate = (NameOrGroupContainsKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && groupKeyword.equals(otherPredicate.groupKeyword);
    }

    /**
     * Returns a string representation of this predicate.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("groupKeyword", groupKeyword)
                .toString();
    }
}
