package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.meeting.Meeting;

/**
 * A UI component that displays the detailed view of a selected {@code Meeting}.
 */
public class MeetingDetailPanel extends UiPart<Region> {

    private static final String FXML = "MeetingDetailPanel.fxml";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox detailContainer;

    @FXML
    private VBox emptyPlaceholder;

    @FXML
    private Label description;

    @FXML
    private Label date;

    @FXML
    private Label time;

    @FXML
    private Label attendees;

    private Meeting currentMeeting;
    private int currentIndex;

    /**
     * Creates a {@code MeetingDetailPanel}.
     */
    public MeetingDetailPanel() {
        super(FXML);
        showEmptyState();
    }

    /**
     * Updates the detailed view to show the given meeting.
     * If the meeting is null, shows the empty state.
     */
    public void updateMeeting(Meeting meeting, int index) {
        if (meeting == null) {
            this.currentMeeting = null;
            showEmptyState();
            return;
        }

        this.currentMeeting = meeting;
        this.currentIndex = index;

        showContentState();

        description.setText(meeting.getDescription());
        date.setText(meeting.getDate().toString());
        time.setText(meeting.getStartTime() + " - " + meeting.getEndTime());

        String attendeesStr = meeting.getAttendees().stream()
                .map(p -> p.getName().fullName)
                .collect(Collectors.joining("\n"));
        attendees.setText(attendeesStr.isEmpty() ? "None" : attendeesStr);
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
}
