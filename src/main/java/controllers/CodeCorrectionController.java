package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CodeCorrectionController {

    @FXML private TextArea txtCode;
    @FXML private Label lblResult;

    @FXML
    private void handleVerify() {
        String code = txtCode.getText();
        // Vérification simple d'un point-virgule manquant
        if (code.contains("System.out.println(\"Hello\");")) {
            lblResult.setText("Bravo ! Code corrigé.");
            lblResult.setStyle("-fx-text-fill: #4dff4d;");
        } else {
            lblResult.setText("Il manque encore quelque chose...");
            lblResult.setStyle("-fx-text-fill: #ff4d4d;");
        }
    }
}
