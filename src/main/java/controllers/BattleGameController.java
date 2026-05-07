package controllers;

import entities.Battle;
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
import services.ServiceBattle;
import services.ServiceJoueur;

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

    // Nouvelles variables pour le système de compétition
    private String battleMode = "IA"; // IA, DUEL, TEAM
    private int currentBattleId = -1;
    private int currentUserId = 1; // À remplacer par l'ID de l'utilisateur connecté
    private ServiceBattle serviceBattle = new ServiceBattle();
    private ServiceJoueur serviceJoueur = new ServiceJoueur();

    private boolean goUp, goDown, goLeft, goRight;
    private double playerX = 100, playerY = 400;
    private final double SPEED = 7.0;
    private double bobbing = 0;

    private AudioClip marching, intro;
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
            marching.setVolume(0.3);
            intro.setVolume(0.5);
        } catch (Exception e) {
            System.out.println("Erreur sons.");
        }
    }

    // Sélection du mode
    @FXML private void selectModeIA() { this.battleMode = "IA"; handleStartGame(); }
    @FXML private void selectModeDuel() { this.battleMode = "DUEL"; handleStartGame(); }
    @FXML private void selectModeTeam() { this.battleMode = "TEAM"; handleStartGame(); }

    @FXML
    private void handleStartGame() {
        startMenu.setVisible(false);
        gameStarted = true;
        
        // 1. Créer la battle en base de données
        currentBattleId = serviceBattle.createBattle(battleMode);
        
        // 2. Faire rejoindre le joueur
        serviceJoueur.rejoindreBattle(currentUserId, currentBattleId);
        
        // 3. Lancer officiellement (Status: ongoing)
        serviceBattle.startBattle(currentBattleId);

        gameLoop.start();
        if (intro != null) intro.play();
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
            pHP -= 0.34;
            playerHealth.setProgress(pHP);
        }
        
        // Mise à jour Score + Ranking en base de données
        lblScore.setText("INTEL: " + score);
        serviceJoueur.updateScore(currentUserId, currentBattleId, score);
        
        qIdx++;
        if (pHP > 0.01 && bHP > 0.01) loadQuestion();
        else endGame();
    }

    private void endGame() {
        gameStarted = false;
        if (gameLoop != null) gameLoop.stop();
        if (qTimeline != null) qTimeline.stop();
        stopAllSounds();
        
        // Finaliser la battle en base de données (Calcul du gagnant)
        serviceBattle.finishBattle(currentBattleId);

        questionPanel.setVisible(false);
        badgeOverlay.setVisible(true);
        
        if (score > bestScore) {
            bestScore = score;
            lblBestScore.setText("RECORD: " + bestScore);
        }
        
        lblResultTitle.setText(bHP <= 0.05 ? "MISSION ACCOMPLIE !" : "MISSION ÉCHOUÉE");
        lblFinalScore.setText("SCORE: " + score + " | MODE: " + battleMode);

        updateBadgeDisplay();
    }

    private void updateBadgeDisplay() {
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
        } catch (Exception e) {}
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
        if (dist < 150 && !inCombat) {
            inCombat = true;
            questionPanel.setVisible(true);
            loadQuestion();
        }
    }

    private void setMove(KeyCode code, boolean state) {
        if (!gameStarted || inCombat) { goUp = goDown = goLeft = goRight = false; return; }
        switch (code) {
            case UP: case W: goUp = state; break;
            case DOWN: case S: goDown = state; break;
            case LEFT: case A: goLeft = state; break;
            case RIGHT: case D: goRight = state; break;
        }
        if (state && marching != null && !marching.isPlaying()) marching.play();
        else if (!state && !goUp && !goDown && !goLeft && !goRight && marching != null) marching.stop();
    }

    private void stopAllSounds() { if (marching != null) marching.stop(); if (intro != null) intro.stop(); }

    @FXML private void handleQuit() { stopAllSounds(); ((Stage) mainPane.getScene().getWindow()).close(); }

    @FXML private void handleRestart() {
        pHP = 1.0; bHP = 1.0; score = 0; qIdx = 0; playerX = 100; playerY = 400;
        playerHealth.setProgress(1.0); botHealth.setProgress(1.0); lblScore.setText("INTEL: 0");
        badgeOverlay.setVisible(false); inCombat = false;
        playerAvatar.setLayoutX(playerX); playerAvatar.setLayoutY(playerY);
        startMenu.setVisible(true);
    }

    private void setupQuestions() {
        questions.add(new Question("Le port SQL par défaut est :", new String[]{"80", "3306", "443"}, "3306"));
        questions.add(new Question("Java est un langage :", new String[]{"Compilé", "Interprété", "Les deux"}, "Les deux"));
        questions.add(new Question("Quel mot clé crée une constante ?", new String[]{"static", "final", "const"}, "final"));
    }
}
