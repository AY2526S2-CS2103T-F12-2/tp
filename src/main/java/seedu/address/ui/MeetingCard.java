package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.meeting.Meeting;

/**
 * A UI component that displays information of a {@code Meeting}.
 */
public class MeetingCard extends UiPart<Region> {
    private static final String FXML = "MeetingListCard.fxml";

    public final Meeting meeting;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private Label attendees;

    /**
     * Creates a {@code MeetingCard} with the given {@code Meeting} and index to display.
     */
    public MeetingCard(Meeting meeting, int displayedIndex) {
        super(FXML);
        this.meeting = meeting;
        id.setText(displayedIndex + ". ");
        description.setText(meeting.getDescription());
        date.setText("Date: " + meeting.getDate().toString());
        time.setText("Time: " + meeting.getStartTime() + " - " + meeting.getEndTime());
        String attendeesStr = meeting.getAttendees().stream()
                .map(p -> p.getName().fullName)
                .collect(Collectors.joining(", "));
        attendees.setText("Attendees: " + attendeesStr);
    }
}
