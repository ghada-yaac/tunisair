package Controller;

import DAO.DaoAvion;
import Entity.Avion;
import Entity.TEtatAvion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.time.LocalDate;

public class AircraftController {

    @FXML private TextField searchField;
    @FXML private Button addAircraftBtn;
    @FXML private VBox formContainer;
    @FXML private Label formTitle;
    @FXML private TextField matriculeField;
    @FXML private ComboBox<String> modeleComboBox;
    @FXML private TextField capaciteField;
    @FXML private ComboBox<TEtatAvion> etatComboBox;
    @FXML private DatePicker dateMaintenancePicker;
    @FXML private Button enregistrerBtn;
    @FXML private Button annulerBtn;
    @FXML private TableView<Avion> aircraftsTable;
    @FXML private TableColumn<Avion, String> matriculeColumn;
    @FXML private TableColumn<Avion, String> modeleColumn;
    @FXML private TableColumn<Avion, Integer> capaciteColumn;
    @FXML private TableColumn<Avion, TEtatAvion> etatColumn;
    @FXML private TableColumn<Avion, Date> maintenanceColumn;
    @FXML private TableColumn<Avion, Void> actionColumn;

    private ObservableList<Avion> aircraftsList;
    private FilteredList<Avion> filteredAircraftsList;
    private Avion selectedAircraft;

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        maintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateProchaineMaintenance"));

        // Initialiser la liste des avions
        aircraftsList = FXCollections.observableArrayList(DaoAvion.lister());
        filteredAircraftsList = new FilteredList<>(aircraftsList, p -> true);
        aircraftsTable.setItems(filteredAircraftsList);

        // Initialiser les ComboBox
        modeleComboBox.setItems(FXCollections.observableArrayList(Avion.getModeles()));
        etatComboBox.setItems(FXCollections.observableArrayList(TEtatAvion.values()));

        // Configurer l'action sur la sélection d'un modèle
        modeleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                capaciteField.setText(String.valueOf(Avion.getCapacite(newVal)));
            }
        });

        // Configurer la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAircraftsList.setPredicate(avion -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return avion.getMatricule().toLowerCase().contains(lowerCaseFilter) ||
                        avion.getModele().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(avion.getCapacite()).contains(lowerCaseFilter) ||
                        avion.getEtat().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Configurer la colonne d'actions
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button archiveButton = new Button("Archiver");

            {
                editButton.setOnAction(event -> {
                    Avion avion = getTableView().getItems().get(getIndex());
                    showFormForEdit(avion);
                });

                archiveButton.setOnAction(event -> {
                    Avion avion = getTableView().getItems().get(getIndex());
                    handleArchive(avion);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, editButton, archiveButton);
                    setGraphic(hbox);
                }
            }
        });

        // Bouton d'ajout
        addAircraftBtn.setOnAction(event -> showFormForAdd());
    }

    private void showFormForAdd() {
        formTitle.setText("Ajouter un avion");
        matriculeField.setText("");
        modeleComboBox.getSelectionModel().clearSelection();
        capaciteField.setText("");
        etatComboBox.getSelectionModel().clearSelection();
        dateMaintenancePicker.setValue(null);
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedAircraft = null;
    }

    private void showFormForEdit(Avion avion) {
        formTitle.setText("Modifier un avion");
        matriculeField.setText(avion.getMatricule());
        modeleComboBox.getSelectionModel().select(avion.getModele());
        capaciteField.setText(String.valueOf(avion.getCapacite()));
        etatComboBox.getSelectionModel().select(avion.getEtat());
        dateMaintenancePicker.setValue(avion.getDateProchaineMaintenance().toLocalDate());
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedAircraft = avion;
    }

    @FXML
    private void handleSave() {
        String matricule = matriculeField.getText();
        String modele = modeleComboBox.getValue();
        int capacite;
        try {
            capacite = Integer.parseInt(capaciteField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide.");
            return;
        }
        TEtatAvion etat = etatComboBox.getValue();
        LocalDate dateMaintenance = dateMaintenancePicker.getValue();

        if (matricule.isEmpty() || modele == null || etat == null || dateMaintenance == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        Avion avion = new Avion(
                matricule,
                modele,
                capacite,
                etat,
                Date.valueOf(dateMaintenance),
                false // Par défaut non archivé
        );

        if (selectedAircraft == null) {
            // Ajouter un nouvel avion
            if (DaoAvion.ajouter(avion)) {
                aircraftsList.add(avion);
                hideForm();
            } else {
                showAlert("Erreur", "Échec de l'ajout de l'avion.");
            }
        } else {
            // Modifier un avion existant
            if (DaoAvion.modifier(avion)) {
                aircraftsList.remove(selectedAircraft);
                aircraftsList.add(avion);
                hideForm();
            } else {
                showAlert("Erreur", "Échec de la modification de l'avion.");
            }
        }
    }

    @FXML
    private void hideForm() {
        formContainer.setVisible(false);
        formContainer.setManaged(false);
        selectedAircraft = null;
    }

    private void handleArchive(Avion avion) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation d'archivage");
        confirm.setHeaderText("Voulez-vous vraiment archiver cet avion ?");
        confirm.setContentText("Matricule: " + avion.getMatricule() + "\nModèle: " + avion.getModele());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (DaoAvion.archiver(avion)) {
                aircraftsList.remove(avion);
            } else {
                showAlert("Erreur", "Échec de l'archivage de l'avion.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}