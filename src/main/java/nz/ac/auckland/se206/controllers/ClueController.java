package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.speech.MP3Player;

/**
 * The ClueController class manages the clue-based scenes in the game, handling interactions with
 * different elements and navigation between clue views.
 */
public class ClueController {

  /** Button to navigate back to the previous scene. */
  @FXML private Button backButton;

  /** Labels for displaying the countdown timer minutes and seconds. */
  @FXML private Label mins;

  @FXML private Label secs;
  @FXML private Label dot;

  /** Rectangle elements representing hints or key clues within the scene. */
  @FXML private Rectangle rectHint;

  @FXML private Rectangle rectBackDrop;

  /** ImageView to display password hints in the clue scene. */
  @FXML private ImageView passwordHint;

  private MP3Player player;

  /**
   * Initializes the clue scene by setting the countdown timer. This method is called automatically
   * after the FXML components have been loaded.
   *
   * @throws ApiProxyException if there is an issue communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs, dot);
    player = new MP3Player("src/main/resources/sounds/button.mp3");
  }

  /**
   * Navigates back to the appropriate room view based on the button that was clicked. Handles
   * navigation to different rooms (room2 or room3).
   *
   * @param event the action event triggered by clicking the go back button
   * @throws ApiProxyException if there is an issue communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    // Get the button that was clicked
    Button clickedButton = (Button) event.getSource();

    // Determine which room to navigate to based on the button's ID
    if (clickedButton.getId().equals("backButton2")) {
      // Navigate to room2
      App.setRoot("room2");
    } else if (clickedButton.getId().equals("backButton3")) {
      // Navigate to room3
      App.setRoot("room3");
    } else {
      // Default navigation to the main room
      App.setRoot("room");
    }

    // Adding click sound effect using MP3Player
    player.play();
  }

  /**
   * Handles the event when the logbook clue is clicked, navigating to the first logbook clue scene.
   *
   * @param event the mouse event triggered by clicking the logbook
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleInitialLogBookClick(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectLogBook")) {
      App.setRoot("logbookclue1");
    }

    // Adding click sound effect using MP3Player
    player.play();
  }

  /**
   * Handles the initial interaction with the computer clue, checking if the password has been
   * guessed and navigating to the appropriate scene (password or clue).
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleInitialComputerClick() throws IOException {
    System.out.println(ComputerClueController.getPasswordHasBeenGuessed());
    if (ComputerClueController.getPasswordHasBeenGuessed()) {
      App.setRoot("computerclue1");
    } else {
      App.setRoot("computercluepassword");
    }
    // Adding click sound effect using MP3Player

    player.play();
  }

  /**
   * Displays the password hint and background when the sticky note is clicked in the computer
   * scene.
   *
   * @param event the mouse event triggered by clicking the sticky note
   * @throws IOException if there is an I/O error
   */
  public void handleComputerStickyNote(MouseEvent event) throws IOException {
    passwordHint.setVisible(true);
    rectBackDrop.setVisible(true);
  }

  /**
   * Hides the password hint and background when the back button is clicked.
   *
   * @param event the mouse event triggered by clicking the back button
   * @throws IOException if there is an I/O error
   */
  public void handleClickBack(MouseEvent event) throws IOException {
    passwordHint.setVisible(false);
    rectBackDrop.setVisible(false);
  }
}
