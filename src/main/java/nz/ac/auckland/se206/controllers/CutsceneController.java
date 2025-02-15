package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.speech.MP3Player;

/**
 * The CutsceneController class manages the introductory cutscene at the start of the game. It
 * displays dialogue from Alice, the game's AI assistant, and introduces the player to the game's
 * objective and suspects. The cutscene transitions to the game scene once the dialogue ends.
 */
public class CutsceneController {
  public static boolean firstTime = false;
  public static TimeManager timeManager =
      TimeManager.getInstance(); // Singleton instance of TimeManager

  public static void setFirstTime(boolean first) {
    firstTime = first;
  }

  @FXML private Label dialogueText;
  @FXML private Label mins;
  @FXML private Label dot;
  @FXML private Label secs;

  @FXML private Button btnNext;

  @FXML private Button btnSkip;

  @FXML private ImageView aliceImage;
  @FXML private ImageView aliceImage2;
  @FXML private ImageView newspaperImage;
  @FXML private ImageView paintingImage;
  @FXML private ImageView williamImage;
  @FXML private ImageView frankImage;
  @FXML private ImageView johnImage;

  @FXML private ProgressBar leftProgressBar;

  private List<String> dialogueLines;
  private int currentDialogueIndex;
  private Timeline progressTimeline;
  private boolean isGameStarted = false;
  private MP3Player player;
  private MP3Player player2;
  private MP3Player player3;
  private MP3Player player4;
  private MP3Player player5;
  private MP3Player player6;

  /**
   * Constructs a new CutsceneController object with default values for dialogue index, progress
   * bars, and dialogue lines.
   */
  public CutsceneController() {
    this.currentDialogueIndex = 0;
    this.leftProgressBar = new ProgressBar();
    this.dialogueLines = new ArrayList<>();
  }

  /** Initializes the cutscene by setting the timer label and displaying the first dialogue line. */
  @FXML
  public void initialize() {
    timeManager.setTimerLabel(mins, secs, dot);

    // disable the skip button
    btnSkip.setDefaultButton(false);
    btnSkip.setFocusTraversable(false);

    currentDialogueIndex = 0;
    // List of dialogue lines for Alice and the player
    dialogueLines = new ArrayList<>();
    dialogueLines.add(
        "Alice: Your mission is to find out who stole this famous painting created by the late"
            + "owner of the George St Art gallery Teressa Harris.");
    dialogueLines.add("Alice: There are 3 suspects you need to investigate");
    dialogueLines.add("Alice: Frank the art Curator, son of the late artist Teresa Harris.");
    dialogueLines.add("Alice: William, the head of security of the gallery.");
    dialogueLines.add("Alice: And John the Janitor, known to be an ex-convict.");
    dialogueLines.add("Alice: Good luck Agent, the fate of the painting is in your hands.");
    player = new MP3Player("src/main/resources/sounds/cutscene1.mp3");
    player2 = new MP3Player("src/main/resources/sounds/cutscene2.mp3");
    player3 = new MP3Player("src/main/resources/sounds/cutscene3.mp3");
    player4 = new MP3Player("src/main/resources/sounds/cutscene4.mp3");
    player5 = new MP3Player("src/main/resources/sounds/cutscene5.mp3");
    player6 = new MP3Player("src/main/resources/sounds/cutscene6.mp3");
    // Set initial progress bar values (full)
    leftProgressBar.setProgress(1.0);

    if (firstTime) {
      // Display the first line of dialogue
      displayNextDialogue();
    }
  }

  // This method displays the next dialogue in the sequence
  @FXML
  private void onLoadNextDialogue() {
    // Check if there are more dialogue lines to display
    if (currentDialogueIndex < dialogueLines.size()) {
      displayNextDialogue();
    } else {
      // Once the dialogue ends, proceed to the game scene
      startGame();
    }
  }

  // This method skips the cutscene and goes directly to the game
  @FXML
  private void onSkipCutscene() throws IOException {
    stopCurrentPlayer(); // Stop any currently playing MP3
    startGame(); // Directly transition to the game
  }

  // Method to automatically update progress bars and advance the dialogue
  private void startProgressBarAutoSkip() {
    progressTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.10),
                event -> {
                  // Reduce progress on both sides
                  leftProgressBar.setProgress(leftProgressBar.getProgress() - 0.01);

                  // If progress reaches 0, automatically move to the next dialogue
                  if (leftProgressBar.getProgress() <= 0.0) {
                    onLoadNextDialogue();
                  }
                }));

    // Set the cycle count so it stops after both bars reach zero
    progressTimeline.setCycleCount(100); // Adjust as needed for timing
    progressTimeline.play();
  }

  private void displayNextDialogue() {
    // Reset the progress bars for each new dialogue
    leftProgressBar.setProgress(1.0);

    // Start the progress bars animation for auto-skipping
    startProgressBarAutoSkip();

    // Display the current dialogue text
    dialogueText.setText(dialogueLines.get(currentDialogueIndex));

    // Control image visibility based on current dialogue index
    newspaperImage.setVisible(currentDialogueIndex == 0);
    paintingImage.setVisible(currentDialogueIndex == 0);
    aliceImage.setVisible(currentDialogueIndex == 0);
    aliceImage2.setVisible(
        currentDialogueIndex == 1
            || currentDialogueIndex == 2
            || currentDialogueIndex == 3
            || currentDialogueIndex == 4);
    frankImage.setVisible(
        currentDialogueIndex == 2 || currentDialogueIndex == 3 || currentDialogueIndex == 4);
    williamImage.setVisible(currentDialogueIndex == 3 || currentDialogueIndex == 4);
    johnImage.setVisible(currentDialogueIndex == 4);

    if (currentDialogueIndex == 5) {
      aliceImage.setVisible(true);
      frankImage.setVisible(false);
      williamImage.setVisible(false);
      johnImage.setVisible(false);
    }

    // Stop any currently playing MP3
    stopCurrentPlayer();

    // Play the corresponding audio file for each dialogue line
    switch (currentDialogueIndex) {
      case 0:
        player.play();
        break;
      case 1:
        player2.play();
        break;
      case 2:
        player3.play();
        break;
      case 3:
        player4.play();
        break;
      case 4:
        player5.play();
        break;
      case 5:
        player6.play();
        break;
      default:
        break;
    }

    // Increment the index for the next line
    currentDialogueIndex++;

    // Disable the "Next" button during dialogue transitions for smoother flow
    btnNext.setDisable(true);

    // Add a slight pause before enabling the "Next" button again
    PauseTransition pause = new PauseTransition(Duration.seconds(2)); // Adjust duration if needed
    pause.setOnFinished(event -> btnNext.setDisable(false));
    pause.play();
  }

  // This method stops the currently playing audio
  private void stopCurrentPlayer() {
    // Stop any currently playing audio
    if (player != null && player.isPlaying()) {
      player.stop();
    }
    if (player2 != null && player2.isPlaying()) {
      player2.stop();
    }

    if (player3 != null && player3.isPlaying()) {
      player3.stop();
    }

    if (player4 != null && player4.isPlaying()) {
      player4.stop();
    }
    if (player5 != null && player5.isPlaying()) {
      player5.stop();
    }
    if (player6 != null && player6.isPlaying()) {
      player6.stop();
    }
  }

  /**
   * Handles the "Enter" key press to skip the current dialogue.
   *
   * @param event the KeyEvent triggered by pressing a key
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleEnterKey(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER && !btnNext.isDisabled()) {
      if (progressTimeline != null) {
        progressTimeline.stop(); // Stop the auto-skip progress bar timeline
      }
      stopCurrentPlayer(); // Stop any currently playing audio
      onLoadNextDialogue(); // Trigger the next dialogue
    }
  }

  // This method handles the transition to the actual game start scene
  private void startGame() {
    if (progressTimeline != null) {
      progressTimeline.stop(); // Stop the timeline if it's running
    }

    if (!isGameStarted) {
      isGameStarted = true;
      try {
        System.out.println("Starting game...");
        App.setRoot("room"); // Assuming 'room' is the first game scene
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Game already started.");
      return;
    }
  }
}
