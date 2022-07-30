
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the JavaFX App class that creates the main structure of te program.
 * This class is also used to pass information between scenes.
 * 
 * @author Nicole Jin 
 * @version 2021-12-06
 */
public class App extends Application {

    private static Scene scene;

    /**
     * The start method is the main entry point for all JavaFX applications.
     * 
     * @param stage is a top-level container that host scenes.
     * @exception IOException if the stream is corrupted or errors occurred during reading the data (connecting to the fxml files).
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 900, 480);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The setRoot method sets the root of the scene that the program is displaying.
     * 
     * @param fxml is the fxml file that the program sets root to.
     * @exception IOException if the stream is corrupted or errors occurred during reading the data (connecting to the fxml files).
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * The loadFXML method gets the base parent subclass.
     * 
     * @param fxml is the fxml file that the program is reading from.
     * @exception IOException if the stream is corrupted or errors occurred during reading the data (connecting to the fxml files).
     * @return fmlLoader.load() is the base class for all nodes that have children.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main class launches the program.
     */
    public static void main(String[] args) {
        launch();
    }

}