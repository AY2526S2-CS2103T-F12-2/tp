package seedu.address.ui;

import java.io.File;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * A utility class for handling profile picture UI updates cleanly.
 */
public class ProfilePictureUtil {

    /**
     * Sets the profile picture onto the given ImageView with a circular clip and center-crop viewport,
     * ensuring it covers the space without distortion.
     * If the picture is absent or invalid, it hides the image and shows the fallback avatar initial.
     */
    public static void setProfilePicture(String picPath, ImageView profilePicView, Label avatarInitial) {
        double fitW = profilePicView.getFitWidth();
        double radius = fitW / 2.0;

        // Apply circular clip
        Circle clip = new Circle(radius, radius, radius);
        profilePicView.setClip(clip);

        if (picPath != null && !picPath.isEmpty()) {
            File f = new File(picPath);
            if (f.exists()) {
                Image img = new Image(f.toURI().toString());

                // Keep image ratio and center crop
                double w = img.getWidth();
                double h = img.getHeight();
                double min = Math.min(w, h);
                double x = (w - min) / 2;
                double y = (h - min) / 2;

                // Set precise square viewpor
                profilePicView.setViewport(new Rectangle2D(x, y, min, min));
                profilePicView.setImage(img);
                profilePicView.setVisible(true);
                avatarInitial.setVisible(false);
                return;
            }
        }

        profilePicView.setVisible(false);
        avatarInitial.setVisible(true);
    }
}
