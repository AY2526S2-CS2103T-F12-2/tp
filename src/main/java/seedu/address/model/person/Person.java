package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Position> positions = new HashSet<>();
    private final Set<Major> majors = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Group> groups = new HashSet<>();
    // We ensure availableHours only ever contains 0 or 1 object.
    private final Set<AvailableHours> availableHours = new HashSet<>();


    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Set<Position> positions,
                  Set<Major> majors, Set<Group> groups, Set<AvailableHours> availableHours) {
        requireAllNonNull(name, phone, email, address, positions, majors, tags, groups, availableHours);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.positions.addAll(positions);
        this.majors.addAll(majors);
        this.tags.addAll(tags);
        this.groups.addAll(groups);
        this.availableHours.addAll(availableHours);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable position set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Position> getPositions() {
        return Collections.unmodifiableSet(positions);
    }

    /**
     * Returns an immutable major set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Major> getMajors() {
        return Collections.unmodifiableSet(majors);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable group set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    /**
     * Returns an immutable {@code Set<AvailableHours>}, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<AvailableHours> getAvailableHours() {
        return Collections.unmodifiableSet(availableHours);
    }


    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && positions.equals(otherPerson.positions)
                && majors.equals(otherPerson.majors)
                && groups.equals(otherPerson.groups)
                && availableHours.equals(otherPerson.availableHours);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, groups, majors, positions, availableHours);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("positions", positions)
                .add("majors", majors)
                .add("groups", groups)
                .add("available hours", availableHours)
                .toString();
    }

}
