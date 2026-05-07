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
        openWindow("/fxml/Quiz.fxml", "WORKSHOP - Quiz Interactif");
    }

    @FXML
    private void handlePlayCode(ActionEvent event) {
        openWindow("/fxml/CodeCorrection.fxml", "WORKSHOP - Correction de Code");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/LoginSelection.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("WORKSHOP - Portail de Connexion");
            stage.setScene(new Scene(root, 800, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlayBattle(ActionEvent event) {
        openWindow("/fxml/BattleGame.fxml", "WORKSHOP - Opération Warzone");
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
