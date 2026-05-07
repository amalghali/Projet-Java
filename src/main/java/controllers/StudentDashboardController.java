package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentDashboardController {

    @FXML
    private void handlePlayQuiz(ActionEvent event) {
        openWindow("/fxml/Quiz.fxml", "Quiz Éducatif");
    }

    @FXML
    private void handlePlayCode(ActionEvent event) {
        openWindow("/fxml/CodeCorrection.fxml", "Correction de Code IA");
    }

    @FXML
    private void handlePlayBattle(ActionEvent event) {
        openWindow("/fxml/BattleGame.fxml", "Battle Royale Informatique");
    }

    @FXML
    private void handleOpenChat(ActionEvent event) {
        openWindow("/fxml/Chatbot.fxml", "Assistant IA");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
