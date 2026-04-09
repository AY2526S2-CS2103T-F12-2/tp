package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.exceptions.WrongTimeFormatException;

/**
 * Container for all compulsory or optional keywords of a person.
 */
public record PersonKeywordSet(List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
                               List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
                               List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
                               List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
                               List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
                               List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
                               List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
                               List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
                               List<String> compulsoryTimeKeywords, List<String> optionalTimeKeywords) {

    /**
     * Constructs a new <code>PersonKeywordSet</code> instance.
     * Every list must be non-null and contain no null elements.
     */
    public PersonKeywordSet {
        requireAllNonNull(compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryTimeKeywords, optionalTimeKeywords);

        ensureNoNullElements(compulsoryNameKeywords);
        ensureNoNullElements(optionalNameKeywords);
        ensureNoNullElements(compulsoryAddressKeywords);
        ensureNoNullElements(optionalAddressKeywords);
        ensureNoNullElements(compulsoryPhoneKeywords);
        ensureNoNullElements(optionalPhoneKeywords);
        ensureNoNullElements(compulsoryMajorKeywords);
        ensureNoNullElements(optionalMajorKeywords);
        ensureNoNullElements(compulsoryEmailKeywords);
        ensureNoNullElements(optionalEmailKeywords);
        ensureNoNullElements(compulsoryTagKeywords);
        ensureNoNullElements(optionalTagKeywords);
        ensureNoNullElements(compulsoryPositionKeywords);
        ensureNoNullElements(optionalPositionKeywords);
        ensureNoNullElements(compulsoryGroupKeywords);
        ensureNoNullElements(optionalGroupKeywords);
        ensureNoNullElements(compulsoryTimeKeywords);
        ensureNoNullElements(optionalTimeKeywords);
    }


    /**
     * Creates a {@code PersonKeywordSet} instance backed by mutable lists for parser accumulation.
     */
    public static PersonKeywordSet withMutableBuckets() {
        return new PersonKeywordSet(
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Adds keyword batches into either compulsory or optional buckets.
     */
    public void addAllKeywords(boolean isCompulsory,
                               List<String> nameKeywords,
                               List<String> groupKeywords,
                               List<String> addressKeywords,
                               List<String> phoneKeywords,
                               List<String> majorKeywords,
                               List<String> emailKeywords,
                               List<String> tagKeywords,
                               List<String> positionKeywords,
                               List<String> timeKeywords) {
        addToBucket(isCompulsory, compulsoryNameKeywords, optionalNameKeywords, nameKeywords);
        addToBucket(isCompulsory, compulsoryGroupKeywords, optionalGroupKeywords, groupKeywords);
        addToBucket(isCompulsory, compulsoryAddressKeywords, optionalAddressKeywords, addressKeywords);
        addToBucket(isCompulsory, compulsoryPhoneKeywords, optionalPhoneKeywords, phoneKeywords);
        addToBucket(isCompulsory, compulsoryMajorKeywords, optionalMajorKeywords, majorKeywords);
        addToBucket(isCompulsory, compulsoryEmailKeywords, optionalEmailKeywords, emailKeywords);
        addToBucket(isCompulsory, compulsoryTagKeywords, optionalTagKeywords, tagKeywords);
        addToBucket(isCompulsory, compulsoryPositionKeywords, optionalPositionKeywords, positionKeywords);
        addToBucket(isCompulsory, compulsoryTimeKeywords, optionalTimeKeywords, timeKeywords);
    }

    private static void addToBucket(boolean isCompulsory,
                                    List<String> compulsoryBucket,
                                    List<String> optionalBucket,
                                    List<String> incomingKeywords) {
        requireNonNull(incomingKeywords);
        ensureNoNullElements(incomingKeywords);
        // Route one parsed keyword batch into either the compulsory or optional side.
        if (isCompulsory) {
            compulsoryBucket.addAll(incomingKeywords);
        } else {
            optionalBucket.addAll(incomingKeywords);
        }
    }

    private static void ensureNoNullElements(List<String> keywords) {
        requireAllNonNull(keywords);
    }

    /**
     * Checks if all non-time slot keywords are valid.
     * A valid keyword is a non-empty string.
     */
    public boolean areAllNonTimeKeywordsValid() {
        return areNonTimeKeywordsValid(compulsoryNameKeywords)
                && areNonTimeKeywordsValid(optionalNameKeywords)
                && areNonTimeKeywordsValid(compulsoryAddressKeywords)
                && areNonTimeKeywordsValid(optionalAddressKeywords)
                && areNonTimeKeywordsValid(compulsoryPhoneKeywords)
                && areNonTimeKeywordsValid(optionalPhoneKeywords)
                && areNonTimeKeywordsValid(compulsoryMajorKeywords)
                && areNonTimeKeywordsValid(optionalMajorKeywords)
                && areNonTimeKeywordsValid(compulsoryEmailKeywords)
                && areNonTimeKeywordsValid(optionalEmailKeywords)
                && areNonTimeKeywordsValid(compulsoryTagKeywords)
                && areNonTimeKeywordsValid(optionalTagKeywords)
                && areNonTimeKeywordsValid(compulsoryPositionKeywords)
                && areNonTimeKeywordsValid(optionalPositionKeywords)
                && areNonTimeKeywordsValid(compulsoryGroupKeywords)
                && areNonTimeKeywordsValid(optionalGroupKeywords);
    }

    /**
     * Checks if all time slot keywords are valid.
     * A valid time slot keyword is either a valid time slot string or a valid time string.
     */
    public boolean areAllTimeKeywordsValid() {
        return areTimeKeywordsValid(compulsoryTimeKeywords)
                && areTimeKeywordsValid(optionalTimeKeywords);
    }


    private static boolean areNonTimeKeywordsValid(List<String> keywords) {
        return keywords.stream().noneMatch(keyword -> keyword.trim().isEmpty());
    }

    private static boolean areTimeKeywordsValid(List<String> keywords) {
        for (String keyword : keywords) {
            // Allow both explicit slots (HHMM-HHMM) and single times (HHMM).
            if (TimeSlot.isValidTimeSlot(keyword)) {
                continue;
            }
            try {
                TimeSlot.stringToTime(keyword);
            } catch (WrongTimeFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all optional keyword buckets are empty.
     */
    public boolean areAllOptionalKeywordsEmpty() {
        return optionalNameKeywords.isEmpty()
                && optionalAddressKeywords.isEmpty()
                && optionalPhoneKeywords.isEmpty()
                && optionalMajorKeywords.isEmpty()
                && optionalEmailKeywords.isEmpty()
                && optionalTagKeywords.isEmpty()
                && optionalPositionKeywords.isEmpty()
                && optionalGroupKeywords.isEmpty()
                && optionalTimeKeywords.isEmpty();
    }

    /**
     * Checks if all compulsory keyword buckets are empty.
     */
    public boolean areAllCompulsoryKeywordsEmpty() {
        return compulsoryNameKeywords.isEmpty()
                && compulsoryAddressKeywords.isEmpty()
                && compulsoryPhoneKeywords.isEmpty()
                && compulsoryMajorKeywords.isEmpty()
                && compulsoryEmailKeywords.isEmpty()
                && compulsoryTagKeywords.isEmpty()
                && compulsoryPositionKeywords.isEmpty()
                && compulsoryGroupKeywords.isEmpty()
                && compulsoryTimeKeywords.isEmpty();
    }

    /**
     * Checks if all fuzzy keyword buckets (name, address, phone, email) are empty.
     */
    public boolean areAllFuzzyKeywordsEmpty() {
        return compulsoryNameKeywords.isEmpty() && optionalNameKeywords.isEmpty()
                && compulsoryAddressKeywords.isEmpty() && optionalAddressKeywords.isEmpty()
                && compulsoryPhoneKeywords.isEmpty() && optionalPhoneKeywords.isEmpty()
                && compulsoryEmailKeywords.isEmpty() && optionalEmailKeywords.isEmpty();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("compulsoryNameKeywords", compulsoryNameKeywords())
                .add("optionalNameKeywords", optionalNameKeywords())
                .add("compulsoryAddressKeywords", compulsoryAddressKeywords())
                .add("optionalAddressKeywords", optionalAddressKeywords())
                .add("compulsoryPhoneKeywords", compulsoryPhoneKeywords())
                .add("optionalPhoneKeywords", optionalPhoneKeywords())
                .add("compulsoryMajorKeywords", compulsoryMajorKeywords())
                .add("optionalMajorKeywords", optionalMajorKeywords())
                .add("compulsoryEmailKeywords", compulsoryEmailKeywords())
                .add("optionalEmailKeywords", optionalEmailKeywords())
                .add("compulsoryTagKeywords", compulsoryTagKeywords())
                .add("optionalTagKeywords", optionalTagKeywords())
                .add("compulsoryPositionKeywords", compulsoryPositionKeywords())
                .add("optionalPositionKeywords", optionalPositionKeywords())
                .add("compulsoryGroupKeywords", compulsoryGroupKeywords())
                .add("optionalGroupKeywords", optionalGroupKeywords())
                .add("compulsoryTimeKeywords", compulsoryTimeKeywords())
                .add("optionalTimeKeywords", optionalTimeKeywords())
                .toString();
    }
}
