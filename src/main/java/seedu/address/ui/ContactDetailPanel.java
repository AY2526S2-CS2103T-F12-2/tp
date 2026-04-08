package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.Person;

/**
 * A UI component that displays the detailed view of a selected {@code Person}.
 */
public class ContactDetailPanel extends UiPart<Region> {

    private static final String FXML = "ContactDetailPanel.fxml";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox detailContainer;

    @FXML
    private VBox emptyPlaceholder;

    @FXML
    private StackPane profilePicPane;

    @FXML
    private Label avatarInitial;

    @FXML
    private ImageView profilePicView;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private VBox notesContainer;

    @FXML
    private Label notes;

    @FXML
    private Region notesDivider;

    @FXML
    private Label email;

    @FXML
    private Label address;

    @FXML
    private FlowPane tags;

    @FXML
    private FlowPane groups;

    @FXML
    private Label majors;

    @FXML
    private Label availableHours;

    @FXML
    private Label positions;

    private Consumer<Integer> onDelete;
    private Consumer<String> onEdit;
    private Person currentPerson;
    private int currentIndex;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    /**
     * Creates a {@code ContactDetailPanel}.
     */
    public ContactDetailPanel() {
        super(FXML);
        showEmptyState();
    }

    public void setOnDelete(Consumer<Integer> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnEdit(Consumer<String> onEdit) {
        this.onEdit = onEdit;
    }

    /**
     * Updates the detailed view to show the given person.
     * If the person is null, shows the empty state.
     */
    public void updatePerson(Person person, int index) {
        if (person == null) {
            this.currentPerson = null;
            showEmptyState();
            return;
        }

        this.currentPerson = person;
        this.currentIndex = index;

        showContentState();

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);

        // Tags
        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Groups
        groups.getChildren().clear();
        if (person.getGroups().isEmpty()) {
            Label label = new Label("N/A");
            label.getStyleClass().add("group-tag");
            groups.getChildren().add(label);
        } else {
            person.getGroups().stream()
                    .sorted(Comparator.comparing(group -> group.value))
                    .forEach(group -> {
                        Label label = new Label(group.value);
                        label.getStyleClass().add("group-tag");
                        groups.getChildren().add(label);
                    });
        }

        // Majors
        String majorsText = person.getMajors().stream().map(m -> m.value).collect(Collectors.joining(", "));
        majors.setText(majorsText.isEmpty() ? "N/A" : majorsText);

        // Available Hours
        String availableHoursText = person.getAvailableHours().stream()
                .map(TimeSlot::toString).collect(Collectors.joining(", "));
        availableHours.setText(availableHoursText.isEmpty() ? "N/A" : availableHoursText);

        // Positions
        String positionsText = person.getPositions().stream().map(p -> p.value).collect(Collectors.joining(", "));
        positions.setText(positionsText.isEmpty() ? "N/A" : positionsText);

        if (person.getFollowUp() != null && !person.getFollowUp().value.isEmpty()) {
            notes.setText(person.getFollowUp().value);
            notesContainer.setManaged(true);
            notesContainer.setVisible(true);
            notesDivider.setManaged(true);
            notesDivider.setVisible(true);
        } else {
            notesContainer.setManaged(false);
            notesContainer.setVisible(false);
            notesDivider.setManaged(false);
            notesDivider.setVisible(false);
        }

        // Set avatar initial
        String firstLetter = person.getName().fullName.substring(0, 1).toUpperCase();
        avatarInitial.setText(firstLetter);

        // Load profile picture if available
        String picPath = person.getProfilePicturePath();
        ProfilePictureUtil.setProfilePicture(picPath, profilePicView, avatarInitial);

        // Optional logic for randomizing avatar color could go here, or we can use CSS
    }

    private void showEmptyState() {
        if (scrollPane != null) {
            scrollPane.setVisible(false);
            scrollPane.setManaged(false);
        }
        emptyPlaceholder.setVisible(true);
        emptyPlaceholder.setManaged(true);
    }

    private void showContentState() {
        if (scrollPane != null) {
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
        }
        emptyPlaceholder.setVisible(false);
        emptyPlaceholder.setManaged(false);
    }

    @FXML
    private void handleDelete() {
        if (onDelete != null && currentPerson != null) {
            onDelete.accept(currentIndex);
        }
    }

    @FXML
    private void handleEdit() {
        if (onEdit == null || currentPerson == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("edit ").append(currentIndex);
        sb.append(" n/").append(currentPerson.getName().fullName);
        sb.append(" p/").append(currentPerson.getPhone().value);
        sb.append(" e/").append(currentPerson.getEmail().value);
        sb.append(" a/").append(currentPerson.getAddress().value);

        for (var tag : currentPerson.getTags()) {
            sb.append(" t/").append(tag.tagName);
        }
        for (var position : currentPerson.getPositions()) {
            sb.append(" po/").append(position.value);
        }
        for (var major : currentPerson.getMajors()) {
            sb.append(" m/").append(major.value);
        }
        for (var group : currentPerson.getGroups()) {
            sb.append(" g/").append(group.value);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HHmm");
        for (var ah : currentPerson.getAvailableHours()) {
            sb.append(" h/").append(ah.startTime.format(fmt))
            .append("-").append(ah.endTime.format(fmt));
        }

        onEdit.accept(sb.toString());
    }
}
