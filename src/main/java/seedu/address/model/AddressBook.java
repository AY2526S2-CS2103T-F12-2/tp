package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.meeting.UniqueMeetingList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by identity comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueMeetingList meetings;

    {
        persons = new UniquePersonList();
        meetings = new UniqueMeetingList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the data in {@code toBeCopied}.
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the meeting list with {@code meetings}.
     * {@code meetings} must not contain duplicate meetings.
     */
    public void setMeetings(List<Meeting> meetings) {
        this.meetings.setMeetings(meetings);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setMeetings(newData.getMeetingList());
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a meeting with the same identity as {@code meeting} exists in the address book.
     */
    public boolean hasMeeting(Meeting meeting) {
        requireNonNull(meeting);
        return meetings.contains(meeting);
    }

    /**
     * Adds a person to the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds a meeting to the address book.
     */
    public void addMeeting(Meeting meeting) {
        Meeting meetingToAdd = meeting.getIndex() == 0
                ? meeting.withIndex(meetings.size() + 1)
                : meeting;
        meetings.add(meetingToAdd);
    }

    /**
     * Adds a person to the address book at the specified index.
     */
    public void addPerson(int index, Person p) {
        persons.add(index, p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("meetings", meetings)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Meeting> getMeetingList() {
        return meetings.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddressBook)) {
            return false;
        }
        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && meetings.equals(otherAddressBook.meetings);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(persons, meetings);
    }
}
