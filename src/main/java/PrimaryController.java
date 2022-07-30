import java.io.IOException;
import javafx.fxml.FXML;

/**
 * This is the PrimaryController class that controls the primary scene.
 * 
 * @author Nicole Jin 
 * @version 2021-12-06
 */
public class PrimaryController {

    /**
     * The switchToSecondary method switches the scene to secondary.
     * 
     * @exception IOException if the stream is corrupted or errors occurred during reading the data (connecting to the fxml files).
     */
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
