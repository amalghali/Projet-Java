package controllers;

import entities.Games;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import services.ServiceGames;

import java.util.Optional;

public class AdminGamesController {

    @FXML private TableView<Games> tableGames;
    @FXML private TableColumn<Games, Integer> colId;
    @FXML private TableColumn<Games, String> colType;
    @FXML private TableColumn<Games, String> colDifficulty;
    @FXML private TableColumn<Games, Integer> colTime;
    @FXML private TableColumn<Games, Integer> colScore;

    @FXML private TextField txtSearchId;
    @FXML private VBox formPane;
    @FXML private Label lblFormTitle;
    @FXML private TextField txtId;
    @FXML private TextField txtType;
    @FXML private TextField txtDifficulty;
    @FXML private TextField txtTime;
    @FXML private TextField txtScore;
    @FXML private Button btnSave;

    private ServiceGames serviceGames = new ServiceGames();
    private ObservableList<Games> gamesList = FXCollections.observableArrayList();
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeJeux"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulte"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeLimit"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("scoreMax"));

        // Recherche dynamique : écoute les changements dans le champ texte
        txtSearchId.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        loadGames();
    }

    private void loadGames() {
        gamesList.setAll(serviceGames.getAll());
        tableGames.setItems(gamesList);
    }

    @FXML
    private void handleNavAdd() {
        isEditMode = false;
        lblFormTitle.setText("Ajouter un Jeu");
        clearForm();
        txtId.setEditable(false);
        formPane.setVisible(true);
    }

    @FXML
    private void handleNavModify() {
        Games selected = tableGames.getSelectionModel().getSelectedItem();
        if (selected != null) {
            isEditMode = true;
            lblFormTitle.setText("Modifier le Jeu");
            fillForm(selected);
            txtId.setEditable(false);
            formPane.setVisible(true);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un jeu à modifier.");
        }
    }

    @FXML
    private void handleNavDelete() {
        Games selected = tableGames.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le jeu ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer le jeu ID: " + selected.getId() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceGames.supprimer(selected);
                loadGames();
                formPane.setVisible(false);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un jeu à supprimer.");
        }
    }

    @FXML
    private void handleSave() {
        try {
            String type = txtType.getText();
            String diff = txtDifficulty.getText();
            int time = Integer.parseInt(txtTime.getText());
            int score = Integer.parseInt(txtScore.getText());

            if (isEditMode) {
                int id = Integer.parseInt(txtId.getText());
                Games g = new Games(id, type, diff, time, score);
                serviceGames.modifier(g);
            } else {
                Games g = new Games(type, diff, time, score);
                serviceGames.ajouter(g);
            }

            loadGames();
            formPane.setVisible(false);
            clearForm();
            
            // Notification de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText(isEditMode ? "Jeu modifié avec succès !" : "Jeu ajouté avec succès !");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Valeurs numériques invalides : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur Critique", "Erreur base de données : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        formPane.setVisible(false);
        clearForm();
    }

    @FXML
    private void handleSearch() {
        String searchId = txtSearchId.getText();
        if (!searchId.isEmpty()) {
            try {
                int id = Integer.parseInt(searchId);
                Games g = serviceGames.getById(id);
                if (g != null) {
                    tableGames.setItems(FXCollections.observableArrayList(g));
                } else {
                    tableGames.setItems(FXCollections.observableArrayList());
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID doit être un nombre.");
            }
        } else {
            loadGames();
        }
    }

    @FXML
    private void handleRefresh() {
        loadGames();
        txtSearchId.clear();
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        // Optionnel: peut-être remplir le formulaire automatiquement au clic ?
        Games selected = tableGames.getSelectionModel().getSelectedItem();
        if (selected != null && event.getClickCount() == 2) {
             fillForm(selected);
             formPane.setVisible(true);
        }
    }

    private void clearForm() {
        txtId.clear();
        txtType.clear();
        txtDifficulty.clear();
        txtTime.clear();
        txtScore.clear();
    }

    private void fillForm(Games g) {
        txtId.setText(String.valueOf(g.getId()));
        txtType.setText(g.getTypeJeux());
        txtDifficulty.setText(g.getDifficulte());
        txtTime.setText(String.valueOf(g.getTimeLimit()));
        txtScore.setText(String.valueOf(g.getScoreMax()));
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/LoginSelection.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("WORKSHOP - Portail de Connexion");
            stage.setScene(new javafx.scene.Scene(root, 800, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
