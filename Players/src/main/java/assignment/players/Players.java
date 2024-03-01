package assignment.players;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Ouda
 */
public class Players extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("players-view.fxml"));

        Scene scene = new Scene(root);

        System.out.println("hello\n");

        stage.getIcons().add(new Image("file:Players/src/main/resources/assignment/players/images/UMIcon.png"));
        stage.setTitle("Players Portal");

        stage.setScene(scene);
        stage.show();

        System.out.println("hello2\n");



    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
