package controllers;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;

public class BattleGameController {

    @FXML private BorderPane mainPane;
    @FXML private Pane arena;
    @FXML private ImageView playerAvatar, botAvatar, imgBadge;
    @FXML private ProgressBar playerHealth, botHealth;
    @FXML private Label lblScore, lblBestScore, lblQuestion, lblTimer, lblResultTitle, lblBadgeLarge, lblFinalScore;
    @FXML private VBox questionPanel, badgeOverlay, startMenu;
    @FXML private Button btnOpt1, btnOpt2, btnOpt3;
    @FXML private Circle muzzleFlash;

    private int score = 0;
    private static int bestScore = 0;
    private double pHP = 1.0, bHP = 1.0;
    private int timeLeft = 15;
    private Timeline qTimeline;
    private int qIdx = 0;
    private boolean inCombat = false;
    private boolean gameStarted = false;

    private boolean goUp, goDown, goLeft, goRight;
    private double playerX = 100, playerY = 400;
    private final double SPEED = 7.0;
    private double bobbing = 0;

    private AudioClip gunshot, impact, marching, intro;
    private AnimationTimer gameLoop;

    private List<Question> questions = new ArrayList<>();
    private static class Question { String q; String[] o; String a; Question(String q, String[] o, String a) {this.q=q;this.o=o;this.a=a;}}

    @FXML
    public void initialize() {
        setupQuestions();
        lblBestScore.setText("RECORD: " + bestScore);
        loadSounds();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameStarted) {
                    updateMovement();
                    checkEncounter();
                }
            }
        };

        Platform.runLater(() -> {
            if (mainPane.getScene() != null) {
                mainPane.getScene().setOnKeyPressed(e -> setMove(e.getCode(), true));
                mainPane.getScene().setOnKeyReleased(e -> setMove(e.getCode(), false));
            }
        });
    }

    private void loadSounds() {
        try {
            marching = new AudioClip(getClass().getResource("/sounds/freesound_community-marching-loop-32908.mp3").toString());
            intro = new AudioClip(getClass().getResource("/sounds/u_wxn5lzrjy3-military-radio-communication-222904 (1).mp3").toString());
            marching.setCycleCount(AudioClip.INDEFINITE);
            intro.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.out.println("Erreur de chargement des sons.");
        }
    }

    @FXML
    private void handleStartGame() {
        startMenu.setVisible(false);
        gameStarted = true;
        gameLoop.start();
        if (intro != null) intro.play();
    }

    @FXML
    private void handleQuit() {
        stopAllSounds();
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }

    private void stopAllSounds() {
        if (marching != null) marching.stop();
        if (intro != null) intro.stop();
    }

    private void setMove(KeyCode code, boolean state) {
        if (!gameStarted || inCombat) { goUp = goDown = goLeft = goRight = false; return; }
        switch (code) {
            case UP: case W: goUp = state; break;
            case DOWN: case S: goDown = state; break;
            case LEFT: case A: goLeft = state; break;
            case RIGHT: case D: goRight = state; break;
        }
        
        if (state && marching != null && !marching.isPlaying()) {
            marching.play();
        } else if (!state && !goUp && !goDown && !goLeft && !goRight && marching != null) {
            if (!inCombat) marching.stop();
        }
    }

    private void updateMovement() {
        boolean moving = false;
        if (goUp && playerY > 200) { playerY -= SPEED; moving = true; }
        if (goDown && playerY < arena.getHeight() - 130) { playerY += SPEED; moving = true; }
        if (goLeft && playerX > 0) { playerX -= SPEED; moving = true; }
        if (goRight && playerX < arena.getWidth() - 110) { playerX += SPEED; moving = true; }
        
        if (moving) {
            bobbing += 0.2;
            playerAvatar.setLayoutY(playerY + Math.sin(bobbing) * 5);
        } else {
            playerAvatar.setLayoutY(playerY);
        }
        playerAvatar.setLayoutX(playerX);
        muzzleFlash.setCenterX(playerX + 110);
        muzzleFlash.setCenterY(playerY + 40);
    }

    private void checkEncounter() {
        double dist = Math.sqrt(Math.pow(playerX - botAvatar.getLayoutX(), 2) + Math.pow(playerY - botAvatar.getLayoutY(), 2));
        if (dist < 150) startCombat();
    }

    private void startCombat() {
        inCombat = true;
        questionPanel.setVisible(true);
        if (marching != null && !marching.isPlaying()) marching.play();
        loadQuestion();
    }

    private void loadQuestion() {
        if (qIdx >= questions.size() || pHP <= 0.01 || bHP <= 0.01) { endGame(); return; }
        Question q = questions.get(qIdx);
        lblQuestion.setText(q.q);
        btnOpt1.setText(q.o[0]);
        btnOpt2.setText(q.o[1]);
        btnOpt3.setText(q.o[2]);
        
        timeLeft = 15;
        lblTimer.setText(String.valueOf(timeLeft));
        if (qTimeline != null) qTimeline.stop();
        qTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            lblTimer.setText(String.valueOf(timeLeft));
            if (timeLeft <= 0) handleAnswer(null);
        }));
        qTimeline.setCycleCount(15);
        qTimeline.play();
    }

    @FXML
    private void handleAnswer(ActionEvent event) {
        if (qTimeline != null) qTimeline.stop();
        boolean correct = false;
        if (event != null) {
            Button b = (Button) event.getSource();
            if (b.getText().equals(questions.get(qIdx).a)) correct = true;
        }

        if (correct) {
            muzzleFlash.setVisible(true);
            new PauseTransition(Duration.millis(100)).setOnFinished(e -> muzzleFlash.setVisible(false));
            bHP -= 0.34;
            botHealth.setProgress(bHP);
            score += 500;
        } else {
            shakeScreen();
            pHP -= 0.34;
            playerHealth.setProgress(pHP);
        }
        
        lblScore.setText("INTEL: " + score);
        qIdx++;
        
        if (pHP > 0.01 && bHP > 0.01) loadQuestion();
        else endGame();
    }

    private void shakeScreen() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), arena);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void endGame() {
        if (qTimeline != null) qTimeline.stop();
        gameStarted = false; // Arrêt du jeu
        if (gameLoop != null) gameLoop.stop();
        stopAllSounds();
        inCombat = true;
        questionPanel.setVisible(false);
        badgeOverlay.setVisible(true);
        
        if (score > bestScore) {
            bestScore = score;
            lblBestScore.setText("RECORD: " + bestScore);
        }
        
        lblResultTitle.setText(bHP <= 0.05 ? "MISSION ACCOMPLIE !" : "MISSION ÉCHOUÉE");
        lblFinalScore.setText("SCORE: " + score + " | RECORD: " + bestScore);

        // Sélection du grade selon vos règles exactes
        try {
            if (score >= 1000) {
                lblBadgeLarge.setText("GOLD COMMANDER 🎖");
                lblBadgeLarge.setStyle("-fx-text-fill: #ffdb4d; -fx-font-weight: bold; -fx-font-size: 50;");
                imgBadge.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/Icons/gold.jpg"))); 
            } else if (score >= 500) {
                lblBadgeLarge.setText("SILVER VETERAN 🎖");
                lblBadgeLarge.setStyle("-fx-text-fill: #c0c0c0; -fx-font-weight: bold; -fx-font-size: 50;");
                imgBadge.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/Icons/silver.jpg")));
            } else {
                lblBadgeLarge.setText("BRONZE RECRUIT 🎖");
                lblBadgeLarge.setStyle("-fx-text-fill: #cd7f32; -fx-font-weight: bold; -fx-font-size: 50;");
                imgBadge.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/Icons/bronze.jpg")));
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement icône : " + e.getMessage());
        }

        ScaleTransition st = new ScaleTransition(Duration.seconds(1), imgBadge);
        st.setFromX(0); st.setFromY(0);
        st.setToX(1); st.setToY(1);
        st.play();
    }

    @FXML
    private void handleRestart() {
        pHP = 1.0; bHP = 1.0; score = 0; qIdx = 0;
        playerX = 100; playerY = 400;
        playerHealth.setProgress(1.0);
        botHealth.setProgress(1.0);
        lblScore.setText("INTEL: 0");
        badgeOverlay.setVisible(false);
        inCombat = false;
        playerAvatar.setLayoutX(playerX);
        playerAvatar.setLayoutY(playerY);
        handleStartGame();
    }

    private void setupQuestions() {
        questions.add(new Question("Le port SQL par défaut est :", new String[]{"80", "3306", "443"}, "3306"));
        questions.add(new Question("Java est un langage :", new String[]{"Compilé", "Interprété", "Les deux"}, "Les deux"));
        questions.add(new Question("Quel mot clé crée une constante ?", new String[]{"static", "final", "const"}, "final"));
    }
}
