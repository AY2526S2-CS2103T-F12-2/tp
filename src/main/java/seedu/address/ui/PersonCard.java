package seedu.address.ui;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String EMPTY_FIELD_MESSAGE = "N/A";
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    private final Runnable onDelete;
    private final Consumer<String> onEdit;
    private final Runnable onPicUpload;
    private final int displayedIndex;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Button deleteButton;
    @FXML
    private Label majors;
    @FXML
    private Label timeSlot;
    @FXML
    private FlowPane groups;
    @FXML
    private Label positions;
    @FXML
    private Label avatarInitial;
    @FXML
    private ImageView profilePicView;
    @FXML
    private Button uploadPicButton;
    @FXML
    private StackPane profilePicPane;
    @FXML
    private Label pinIcon;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, Runnable onDelete, Consumer<String> onEdit,
                      Runnable onPicUpload) {
        super(FXML);
        this.person = person;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.onPicUpload = onPicUpload;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        String fullName = person.getName().fullName.trim();
        String initials = fullName.isEmpty() ? "" : fullName.substring(0, 1).toUpperCase();
        avatarInitial.setText(initials);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        String majorsText = person.getMajors().stream().map(m -> m.value).collect(Collectors.joining(", "));
        majors.setText("Major: " + (majorsText.isEmpty() ? EMPTY_FIELD_MESSAGE : majorsText));
        String timeSlotText = person.getAvailableHours().stream()
                .map(TimeSlot::toString).collect(Collectors.joining(", "));
        timeSlot.setText("Available hours: "
                + (timeSlotText.isEmpty() ? EMPTY_FIELD_MESSAGE : timeSlotText));
        String positionsText = person.getPositions().stream().map(p -> p.value).collect(Collectors.joining(", "));
        positions.setText("Position: " + (positionsText.isEmpty() ? EMPTY_FIELD_MESSAGE : positionsText));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        addGroupLabels(person);
        updateProfilePicture(person.getProfilePicturePath());
        pinIcon.setVisible(person.isPinned());
    }

    private void updateProfilePicture(String picPath) {
        if (picPath != null && !picPath.isEmpty()) {
            File f = new File(picPath);
            if (f.exists()) {
                profilePicView.setImage(new Image(f.toURI().toString()));
                profilePicView.setVisible(true);
                profilePicView.setStyle("-fx-cursor: hand;");
                profilePicView.setOnMouseClicked(e -> onPicUpload.run());
                uploadPicButton.setVisible(false);
                return;
            }
        }
        profilePicView.setVisible(false);
        profilePicView.setOnMouseClicked(null);
        uploadPicButton.setVisible(true);
    }

    /**
     * Adds group labels to the card in a sorted order.
     */
    private void addGroupLabels(Person person) {
        if (person.getGroups().isEmpty()) {
            Label label = new Label("N/A");
            label.getStyleClass().add("group-tag");
            groups.getChildren().add(label);
            return;
        }
        person.getGroups().stream()
                .sorted(Comparator.comparing(group -> group.value))
                .forEach(group -> {
                    Label label = new Label(group.value);
                    label.getStyleClass().add("group-tag");
                    groups.getChildren().add(label);
                });
    }

    /**
     * Handles delete button clicks for this card.
     */
    @FXML
    private void handleDelete() {
        onDelete.run();
    }

    @FXML
    private void handleUploadPic() {
        onPicUpload.run();
    }

    @FXML
    private void handleEdit() {
        StringBuilder sb = new StringBuilder();
        sb.append("edit ").append(displayedIndex);
        sb.append(" n/").append(person.getName().fullName);
        sb.append(" p/").append(person.getPhone().value);
        sb.append(" e/").append(person.getEmail().value);
        sb.append(" a/").append(person.getAddress().value);

        for (var tag : person.getTags()) {
            sb.append(" t/").append(tag.tagName);
        }
        for (var position : person.getPositions()) {
            sb.append(" po/").append(position.value);
        }
        for (var major : person.getMajors()) {
            sb.append(" m/").append(major.value);
        }
        for (var group : person.getGroups()) {
            sb.append(" g/").append(group.value);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HHmm");
        for (var ah : person.getAvailableHours()) {
            sb.append(" h/").append(ah.startTime.format(fmt))
            .append("-").append(ah.endTime.format(fmt));
        }

        onEdit.accept(sb.toString());
    }
}
