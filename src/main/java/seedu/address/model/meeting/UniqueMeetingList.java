package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.meeting.exceptions.DuplicateMeetingException;
import seedu.address.model.meeting.exceptions.MeetingNotFoundException;
import seedu.address.model.person.Person;

/**
 * A list of meetings that enforces uniqueness between its elements and does not allow nulls.
 */
public class UniqueMeetingList implements Iterable<Meeting> {

    private final ObservableList<Meeting> internalList = FXCollections.observableArrayList();
    private final ObservableList<Meeting> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent meeting as the given argument.
     */
    public boolean contains(Meeting toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameMeeting);
    }

    /**
     * Adds a meeting to the list.
     * The meeting must not already exist in the list.
     */
    public void add(Meeting toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateMeetingException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the meeting from the list and reassigns meeting indexes.
     * The meeting must exist in the list.
     */
    public void remove(Meeting toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new MeetingNotFoundException();
        }
        reindexMeetings();
    }

    /**
     * Returns the number of meetings currently in the list.
     */
    public int size() {
        return internalList.size();
    }

    /**
     * Replaces the contents of this list with {@code meetings}.
     * {@code meetings} must not contain duplicate meetings.
     */
    public void setMeetings(List<Meeting> meetings) {
        requireAllNonNull(meetings);
        if (!meetingsAreUnique(meetings)) {
            throw new DuplicateMeetingException();
        }
        internalList.setAll(meetings);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Meeting> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Meeting> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UniqueMeetingList)) {
            return false;
        }
        UniqueMeetingList otherUniqueMeetingList = (UniqueMeetingList) other;
        return internalList.equals(otherUniqueMeetingList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    private boolean meetingsAreUnique(List<Meeting> meetings) {
        for (int i = 0; i < meetings.size() - 1; i++) {
            for (int j = i + 1; j < meetings.size(); j++) {
                if (meetings.get(i).isSameMeeting(meetings.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Reassigns meeting indices to remain 1-based and contiguous.
     */
    private void reindexMeetings() {
        for (int i = 0; i < internalList.size(); i++) {
            internalList.set(i, internalList.get(i).withIndex(i + 1));
        }
    }

    /**
     * Replaces attendee {@code target} with {@code editedPerson} across all meetings containing that attendee.
     */
    public void replaceAttendeeInMeetings(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        for (int i = 0; i < internalList.size(); i++) {
            Meeting updatedMeeting = internalList.get(i).withUpdatedAttendee(target, editedPerson);
            if (updatedMeeting != internalList.get(i)) {
                internalList.set(i, updatedMeeting);
            }
        }
    }

    /**
     * Removes attendee {@code target} from all meetings.
     * Meetings that become empty are removed. Remaining meetings are reindexed if needed.
     */
    public void removeAttendeeFromMeetings(Person target) {
        requireNonNull(target);

        boolean hasStructureChanged = false;
        for (int i = internalList.size() - 1; i >= 0; i--) {
            Meeting currentMeeting = internalList.get(i);
            java.util.Optional<Meeting> maybeUpdatedMeeting = currentMeeting.withoutAttendee(target);

            if (maybeUpdatedMeeting.isEmpty()) {
                internalList.remove(i);
                hasStructureChanged = true;
                continue;
            }

            Meeting updatedMeeting = maybeUpdatedMeeting.get();
            if (updatedMeeting != currentMeeting) {
                internalList.set(i, updatedMeeting);
            }
        }

        if (hasStructureChanged) {
            reindexMeetings();
        }
    }
}
