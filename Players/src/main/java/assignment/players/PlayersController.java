package assignment.players;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ouda
 */
public class PlayersController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane PlayerPortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private ComboBox position;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    PlayerRecord playerRec = null;
    int playerPosition = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        DataKey key = new DataKey(this.name.getText(), playerPosition);
        try {
            playerRec = database.find(key);
            showPlayer();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void delete() {
        PlayerRecord previousPlayer = null;
        try {
            previousPlayer = database.predecessor(playerRec.getDataKey());
        } catch (DictionaryException ex) {

        }
        PlayerRecord nextPlayer = null;
        try {
            nextPlayer = database.successor(playerRec.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = playerRec.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.PlayerPortal.setVisible(false);
            displayAlert("No more players in the database to show");
        } else {
            if (previousPlayer != null) {
                playerRec = previousPlayer;
                showPlayer();
            } else if (nextPlayer != null) {
                playerRec = nextPlayer;
                showPlayer();
            }
        }
    }

    private void showPlayer() {
        play.setDisable(false);
        pause.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = playerRec.getImage();
        Image playerImage = new Image("file:Players/src/main/resources/assignment/players/images/" + img);
        image.setImage(playerImage);
        title.setText(playerRec.getDataKey().getPlayerName());
        about.setText(playerRec.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/players/images/UMIcon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getPosition() {
        switch (this.position.getValue().toString()) {
            case "Guard":
                this.playerPosition = 1;
                break;
            case "Forward":
                this.playerPosition = 2;
                break;
            case "Center":
                this.playerPosition = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        try {
            playerRec = database.smallest(); // Assuming there's a method to get the first record
            showPlayer();
        } catch (DictionaryException ex) {
            displayAlert("Error finding the first player: " + ex.getMessage());
        }
    }

    public void last() {
        try {
            playerRec = database.largest(); // Assuming there's a method to get the last record
            showPlayer();
        } catch (DictionaryException ex) {
            displayAlert("Error finding the last player: " + ex.getMessage());
        }
    }

    public void next() {
        if (playerRec == null) {
            first();
            return;
        }
        try {
            PlayerRecord nextPlayer = database.successor(playerRec.getDataKey());
            if (nextPlayer != null) {
                playerRec = nextPlayer;
                showPlayer();
            } else {
                displayAlert("This is the last player in the database.");
            }
        } catch (DictionaryException ex) {
            displayAlert("Error finding the next player: " + ex.getMessage());
        }
    }

    public void previous() {
        if (playerRec == null) {
            last();
            return;
        }
        try {
            PlayerRecord prevPlayer = database.predecessor(playerRec.getDataKey());
            if (prevPlayer != null) {
                playerRec = prevPlayer;
                showPlayer();
            } else {
                displayAlert("This is the first player in the database.");
            }
        } catch (DictionaryException ex) {
            displayAlert("Error finding the previous player: " + ex.getMessage());
        }
    }


    public void play() {
        String fn = "Players/src/main/resources/assignment/players/sounds/" + playerRec.getSound();
        File file = new File(fn);
        System.out.println("Looking for file at: " + file.getAbsolutePath());
        if (file.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
        }

        String filename = "Players/src/main/resources/assignment/players/sounds/" + playerRec.getSound();
        media = new Media(new File(filename).toURI().toString());
        player = new MediaPlayer(media);
        play.setDisable(true);
        pause.setDisable(false);
        player.play();
    }

    public void pause() {
        play.setDisable(false);
        pause.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String playerName = "";
            String description;
            int position = 0;
            input = new Scanner(new File("Players/PlayersDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        position = Integer.parseInt(data);
                        break;
                    case 1:
                        playerName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new PlayerRecord(new DataKey(playerName, position), description, playerName + ".mp3", playerName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: PlayersDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(PlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.PlayerPortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        position.setItems(FXCollections.observableArrayList(
                "Guard", "Forward", "Center"
        ));
        position.setValue("Guard");
    }

}
