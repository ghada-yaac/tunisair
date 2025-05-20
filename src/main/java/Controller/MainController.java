package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentArea;

    @FXML
    private Label pageTitle;

    @FXML
    public void initialize() {
        // Charger le tableau de bord par défaut
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        loadView("/dashboard.fxml");
        pageTitle.setText("Tableau de bord");
    }

    @FXML
    public void showFlights() {
        loadView("/flights-management.fxml");
        pageTitle.setText("Gestion des vols");
    }

    @FXML
    public void showAircrafts() {
        loadView("/aircrafts-management.fxml");
        pageTitle.setText("Gestion des avions");
    }
    @FXML
    public void showPersonnel() {
        loadView("/personnel-management.fxml");
        pageTitle.setText("Gestion du personnel");
    }
    @FXML
    public void showCrews() {
        loadView("/crews-management.fxml");
        pageTitle.setText("Gestion des équipages");
    }

    @FXML
    public void logout() {
        // Logique de déconnexion
        System.out.println("Déconnexion...");
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}