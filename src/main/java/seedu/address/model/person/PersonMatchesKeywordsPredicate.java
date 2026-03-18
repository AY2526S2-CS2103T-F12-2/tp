package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s fields match any of the provided keywords.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> addressKeywords;
    private final List<String> phoneKeywords;
    private final List<String> majorKeywords;
    private final List<String> emailKeywords;
    private final List<String> tagKeywords;
    private final List<String> positionKeywords;
    private final Optional<String> groupKeyword;

    /**
     * Creates a predicate that matches on name, address, phone, major, email, tag, position, or group keywords.
     */
    public PersonMatchesKeywordsPredicate(List<String> nameKeywords, List<String> addressKeywords,
            List<String> phoneKeywords, List<String> majorKeywords, List<String> emailKeywords,
            List<String> tagKeywords, List<String> positionKeywords, Optional<String> groupKeyword) {
        this.nameKeywords = nameKeywords;
        this.addressKeywords = addressKeywords;
        this.phoneKeywords = phoneKeywords;
        this.majorKeywords = majorKeywords;
        this.emailKeywords = emailKeywords;
        this.tagKeywords = tagKeywords;
        this.positionKeywords = positionKeywords;
        this.groupKeyword = groupKeyword;
    }

    /**
     * Returns true if any of the provided keywords match the person.
     */
    @Override
    public boolean test(Person person) {
        return matchesName(person)
                || matchesAddress(person)
                || matchesPhone(person)
                || matchesMajor(person)
                || matchesEmail(person)
                || matchesTags(person)
                || matchesPositions(person)
                || matchesGroup(person);
    }

    /**
     * Returns true if any name keyword matches the person's name.
     */
    private boolean matchesName(Person person) {
        return !nameKeywords.isEmpty()
                && nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
    }

    /**
     * Returns true if any address keyword matches the person's address.
     */
    private boolean matchesAddress(Person person) {
        return !addressKeywords.isEmpty()
                && addressKeywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getAddress().value, keyword));
    }

    /**
     * Returns true if any phone keyword matches the person's phone.
     */
    private boolean matchesPhone(Person person) {
        return !phoneKeywords.isEmpty()
                && phoneKeywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getPhone().value, keyword));
    }

    /**
     * Returns true if any major keyword matches the person's majors.
     */
    private boolean matchesMajor(Person person) {
        return !majorKeywords.isEmpty()
                && person.getMajors().stream()
                .anyMatch(major -> majorKeywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(major.value, keyword)));
    }

    /**
     * Returns true if any email keyword matches the person's email.
     */
    private boolean matchesEmail(Person person) {
        return !emailKeywords.isEmpty()
                && emailKeywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getEmail().value, keyword));
    }

    /**
     * Returns true if any tag keyword matches the person's tags.
     */
    private boolean matchesTags(Person person) {
        return !tagKeywords.isEmpty()
                && person.getTags().stream()
                .anyMatch(tag -> tagKeywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(tag.tagName, keyword)));
    }

    /**
     * Returns true if any position keyword matches the person's positions.
     */
    private boolean matchesPositions(Person person) {
        return !positionKeywords.isEmpty()
                && person.getPositions().stream()
                .anyMatch(position -> positionKeywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(position.value, keyword)));
    }

    /**
     * Returns true if the group keyword matches any group with prefix matching.
     */
    private boolean matchesGroup(Person person) {
        return groupKeyword
                .map(keyword -> hasGroupWithPrefixIgnoreCase(person, keyword))
                .orElse(false);
    }

    /**
     * Returns true if {@code sentence} contains {@code keyword}, ignoring case.
     */
    private boolean containsIgnoreCase(String sentence, String keyword) {
        return sentence.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
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
     * Returns true if both predicates have the same keyword settings.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && addressKeywords.equals(otherPredicate.addressKeywords)
                && phoneKeywords.equals(otherPredicate.phoneKeywords)
                && majorKeywords.equals(otherPredicate.majorKeywords)
                && emailKeywords.equals(otherPredicate.emailKeywords)
                && tagKeywords.equals(otherPredicate.tagKeywords)
                && positionKeywords.equals(otherPredicate.positionKeywords)
                && groupKeyword.equals(otherPredicate.groupKeyword);
    }

    /**
     * Returns a string representation of this predicate.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("addressKeywords", addressKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("majorKeywords", majorKeywords)
                .add("emailKeywords", emailKeywords)
                .add("tagKeywords", tagKeywords)
                .add("positionKeywords", positionKeywords)
                .add("groupKeyword", groupKeyword)
                .toString();
    }
}
