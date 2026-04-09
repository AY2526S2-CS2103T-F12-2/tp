package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Flags: -a for adding fields to existing values, -r for overwriting existing values.\n"
            + "By default existing values will be overwritten, unless -a is used as flag.\n"
            + "Parameters: [FLAG] INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_MAJOR + "MAJOR] "
            + "[" + PREFIX_TIME + "AVAILABLE_HOURS] "
            + "[" + PREFIX_GROUP + "GROUP] "
            + "[" + PREFIX_POSITION + "POSITION] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Note that no other non-blank content should precede the index or flag.\n"
            + "Example: " + COMMAND_WORD + " -r 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "The person with same "
            + "name/email/phone number already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index Index of the person in the filtered person list to edit.
     * @param editPersonDescriptor The details to edit the person with.
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (model.hasPersonExcept(editedPerson, personToEdit)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     * @param personToEdit the original {@code Person} whose details are being edited; must not be null
     * @param editPersonDescriptor holds the new field values and the edit flag
     * @return a new {@code Person} reflecting the applied edits
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<TimeSlot> updatedAvailableHours =
                editPersonDescriptor.getAvailableHours().orElse(personToEdit.getAvailableHours());

        final Set<Tag> updatedTags;
        final Set<Major> updatedMajors;
        final Set<Group> updatedGroups;
        final Set<Position> updatedPositions;

        if (editPersonDescriptor.isAppendFlag()) {
            updatedTags = new HashSet<>(personToEdit.getTags());
            editPersonDescriptor.getTags().ifPresent(updatedTags::addAll);

            updatedMajors = new HashSet<>(personToEdit.getMajors());
            editPersonDescriptor.getMajors().ifPresent(updatedMajors::addAll);

            updatedGroups = new HashSet<>(personToEdit.getGroups());
            editPersonDescriptor.getGroups().ifPresent(updatedGroups::addAll);

            updatedPositions = new HashSet<>(personToEdit.getPositions());
            editPersonDescriptor.getPositions().ifPresent(updatedPositions::addAll);
        } else {
            updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
            updatedMajors = editPersonDescriptor.getMajors().orElse(personToEdit.getMajors());
            updatedGroups = editPersonDescriptor.getGroups().orElse(personToEdit.getGroups());
            updatedPositions = editPersonDescriptor.getPositions().orElse(personToEdit.getPositions());
        }

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, updatedPositions,
                updatedMajors, updatedGroups, updatedAvailableHours, personToEdit.getFollowUp(),
                personToEdit.getProfilePicturePath(), personToEdit.isPinned());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private Set<Major> majors;
        private Set<Position> positions;
        private Set<Group> groups;
        private Set<TimeSlot> availableHours;
        private EditFlag flag = EditFlag.NONE;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setPositions(toCopy.positions);
            setMajors(toCopy.majors);
            setGroups(toCopy.groups);
            setAvailableHours(toCopy.availableHours);
            setEditFlag(toCopy.flag);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, majors,
                    groups, positions, availableHours);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setEditFlag(EditFlag flag) {
            this.flag = flag;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code positions} to this object's {@code positions}.
         * A defensive copy of {@code positions} is used internally.
         */
        public void setPositions(Set<Position> positions) {
            this.positions = (positions != null) ? new HashSet<>(positions) : null;
        }

        /**
         * Returns an unmodifiable position set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code positions} is null.
         */
        public Optional<Set<Position>> getPositions() {
            return (positions != null) ? Optional.of(Collections.unmodifiableSet(positions)) : Optional.empty();
        }

        /**
         * Sets {@code majors} to this object's {@code majors}.
         * A defensive copy of {@code majors} is used internally.
         */
        public void setMajors(Set<Major> majors) {
            this.majors = (majors != null) ? new HashSet<>(majors) : null;
        }

        /**
         * Returns an unmodifiable major set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code majors} is null.
         */
        public Optional<Set<Major>> getMajors() {
            return (majors != null) ? Optional.of(Collections.unmodifiableSet(majors)) : Optional.empty();
        }

        /**
         * Sets {@code groups} to this object's {@code groups}.
         * A defensive copy of {@code groups} is used internally.
         */
        public void setGroups(Set<Group> groups) {
            this.groups = (groups != null) ? new HashSet<>(groups) : null;
        }

        /**
         * Returns an unmodifiable group set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code groups} is null.
         */
        public Optional<Set<Group>> getGroups() {
            return (groups != null) ? Optional.of(Collections.unmodifiableSet(groups)) : Optional.empty();
        }

        /**
         * Sets {@code availableHours} to this object's {@code availableHours}.
         * A defensive copy of {@code availableHours} is used internally.
         */
        public void setAvailableHours(Set<TimeSlot> availableHours) {
            this.availableHours = (availableHours != null) ? new HashSet<>(availableHours) : null;
        }

        /**
         * Returns an unmodifiable available hour set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code availableHours} is null.
         */
        public Optional<Set<TimeSlot>> getAvailableHours() {
            return (availableHours != null)
                    ? Optional.of(Collections.unmodifiableSet(availableHours))
                    : Optional.empty();
        }

        /**
         * Returns whether this command has <code>APPEND</code> flag.
         */
        public boolean isAppendFlag() {
            return this.flag.equals(EditFlag.APPEND);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(groups, otherEditPersonDescriptor.groups)
                    && Objects.equals(majors, otherEditPersonDescriptor.majors)
                    && Objects.equals(positions, otherEditPersonDescriptor.positions)
                    && Objects.equals(availableHours, otherEditPersonDescriptor.availableHours)
                    && Objects.equals(flag, otherEditPersonDescriptor.flag);
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
}
