package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class LoginSelectionController {

    @FXML
    private void handleStudentAccess(ActionEvent event) {
        openWindow(event, "/fxml/StudentDashboard.fxml", "WORKSHOP - Espace Étudiant");
    }

    @FXML
    private void handleAdminAccess(ActionEvent event) {
        openWindow(event, "/fxml/AdminGames.fxml", "WORKSHOP - Administration");
    }

    private void openWindow(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
            // Fermer la fenêtre de sélection
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
