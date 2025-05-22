package Controller;

import java.util.stream.Collectors;
import DAO.DaoAvion;
import DAO.DaoEquipage;
import DAO.DaoVol;
import Entity.Avion;
import Entity.Equipage;
import Entity.TStatut;
import Entity.Vol;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FlightController {

    @FXML private VBox formContainer;
    @FXML private Label formTitle;
    @FXML private TextField codeField;
    @FXML private ComboBox<String> departureCombo;
    @FXML private ComboBox<String> destinationCombo;
    @FXML private DatePicker departureDate;
    @FXML private DatePicker arrivalDate;
    @FXML private ComboBox<TStatut> statusCombo;
    @FXML private ComboBox<String> aircraftCombo;
    @FXML private ComboBox<String> crewCombo;
    @FXML private Button toggleFormBtn;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private TableView<Vol> flightsTable;
    @FXML private TableColumn<Vol, String> codeColumn;
    @FXML private TableColumn<Vol, String> departureColumn;
    @FXML private TableColumn<Vol, String> destinationColumn;
    @FXML private TableColumn<Vol, Date> departureDateColumn;
    @FXML private TableColumn<Vol, TStatut> statusColumn;
    @FXML private TableColumn<Vol, String> aircraftColumn;
    @FXML private TableColumn<Vol, String> crewColumn;
    @FXML private TableColumn<Vol, Void> actionColumn;

    private ObservableList<Vol> flights = FXCollections.observableArrayList();
    private Vol currentFlight = null;

    @FXML private VBox searchContainer;
    @FXML private TextField searchCodeField;
    @FXML private ComboBox<String> searchDepartureCombo;
    @FXML private ComboBox<String> searchDestinationCombo;
    @FXML private DatePicker searchDepartureDate;
    @FXML private ComboBox<TStatut> searchStatusCombo;
    @FXML private ComboBox<String> searchAircraftCombo;
    @FXML private Button toggleSearchBtn;
    @FXML private Button searchBtn;
    @FXML private Button resetSearchBtn;

    private ObservableList<Vol> allFlights = FXCollections.observableArrayList();
    private ObservableList<Vol> filteredFlights = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        flightsTable.setItems(filteredFlights);
        // flightsTable.setItems(flights);  // Cette ligne est CRUCIALE
        // Initialisation des colonnes
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        departureColumn.setCellValueFactory(cellData -> cellData.getValue().lieuDepartProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());
        departureDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateVolProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        aircraftColumn.setCellValueFactory(cellData -> cellData.getValue().avionProperty());
        crewColumn.setCellValueFactory(cellData -> cellData.getValue().equipageProperty());

        // Configuration des boutons d'action
        actionColumn.setCellFactory(createActionCellFactory());

        // Initialisation des combobox
        departureCombo.getItems().addAll(Vol.getVilles());
        destinationCombo.getItems().addAll(Vol.getVilles());
        statusCombo.getItems().addAll(TStatut.values());
        List<Avion> avions = DaoAvion.lister();
        aircraftCombo.getItems().addAll(avions.stream().map(Avion::getMatricule).toList());
        // TODO: Remplir les combobox pour les avions et équipages
        // aircraftCombo.getItems().addAll(...);
        // crewCombo.getItems().addAll(...);
        List<Equipage> equipages = DaoEquipage.lister();
        crewCombo.getItems().addAll(equipages.stream().map(Equipage::getCode).toList());


        // Gestion des événements
        toggleFormBtn.setOnAction(e -> toggleForm(false));
        cancelBtn.setOnAction(e -> toggleForm(false));
        saveBtn.setOnAction(e -> saveFlight());

        searchDepartureCombo.getItems().addAll(Vol.getVilles());
        searchDestinationCombo.getItems().addAll(Vol.getVilles());
        searchStatusCombo.getItems().addAll(TStatut.values());

        searchAircraftCombo.getItems().addAll(avions.stream().map(Avion::getMatricule).toList());
        aircraftCombo.getItems().addAll(avions.stream().map(Avion::getMatricule).toList());

        crewCombo.getItems().addAll(equipages.stream().map(Equipage::getCode).toList());

        // Gestion des événements
        toggleFormBtn.setOnAction(e -> toggleForm(false));
        cancelBtn.setOnAction(e -> toggleForm(false));
        saveBtn.setOnAction(e -> saveFlight());

        // Nouveaux événements pour la recherche
        toggleSearchBtn.setOnAction(e -> toggleSearch());
        searchBtn.setOnAction(e -> searchFlights());
        resetSearchBtn.setOnAction(e -> resetSearch());
        searchCodeField.textProperty().addListener((obs, oldVal, newVal) -> quickSearch(newVal));
        // Chargement des données
        loadFlights();

    }

    private void toggleSearch() {
        boolean isVisible = searchContainer.isVisible();
        searchContainer.setVisible(!isVisible);
        searchContainer.setManaged(!isVisible);
        toggleSearchBtn.setText(isVisible ? "Recherche avancée" : "Masquer recherche");
    }

    private void quickSearch(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            filteredFlights.setAll(allFlights);
            return;
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Vol> filtered = allFlights.stream()
                .filter(vol ->
                        vol.getCode().toLowerCase().contains(lowerKeyword) ||
                                vol.getLieuDepart().toLowerCase().contains(lowerKeyword) ||
                                vol.getDestination().toLowerCase().contains(lowerKeyword) ||
                                vol.getAvion().toLowerCase().contains(lowerKeyword) ||
                                vol.getEquipage().toLowerCase().contains(lowerKeyword) ||
                                vol.getStatut().toString().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());

        filteredFlights.setAll(filtered);
    }

    private void searchFlights() {
        String code = searchCodeField.getText();
        String departure = searchDepartureCombo.getValue();
        String destination = searchDestinationCombo.getValue();
        LocalDate departureDate = searchDepartureDate.getValue();
        TStatut status = searchStatusCombo.getValue();
        String aircraft = searchAircraftCombo.getValue();

        List<Vol> filtered = allFlights.stream()
                .filter(vol -> code == null || code.isEmpty() || vol.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(vol -> departure == null || vol.getLieuDepart().equalsIgnoreCase(departure))
                .filter(vol -> destination == null || vol.getDestination().equalsIgnoreCase(destination))
                .filter(vol -> departureDate == null || vol.getDateVol().toLocalDate().equals(departureDate))
                .filter(vol -> status == null || vol.getStatut() == status)
                .filter(vol -> aircraft == null || vol.getAvion().equalsIgnoreCase(aircraft))
                .collect(Collectors.toList());

        filteredFlights.setAll(filtered);
    }

    private void resetSearch() {
        searchCodeField.clear();
        searchDepartureCombo.getSelectionModel().clearSelection();
        searchDestinationCombo.getSelectionModel().clearSelection();
        searchDepartureDate.setValue(null);
        searchStatusCombo.getSelectionModel().clearSelection();
        searchAircraftCombo.getSelectionModel().clearSelection();

        filteredFlights.setAll(allFlights);
    }

    private void loadFlights() {
        allFlights.clear();
        List<Vol> vols = DaoVol.lister();
        allFlights.addAll(vols);
        filteredFlights.setAll(allFlights);
    }
    private Callback<TableColumn<Vol, Void>, TableCell<Vol, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");

                editBtn.setOnAction(event -> {
                    Vol vol = getTableView().getItems().get(getIndex());
                    editFlight(vol);
                });

                deleteBtn.setOnAction(event -> {
                    Vol vol = getTableView().getItems().get(getIndex());
                    deleteFlight(vol);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        };
    }

    private void toggleForm(boolean isEdit) {
        boolean isVisible = formContainer.isVisible();

        if (!isVisible) {
            formContainer.setVisible(true);
            formContainer.setManaged(true);
            toggleFormBtn.setText("Annuler");

            if (isEdit) {
                formTitle.setText("Modifier le vol");
                saveBtn.setText("Mettre à jour");
            } else {
                formTitle.setText("Ajouter un vol");
                saveBtn.setText("Enregistrer");
                resetForm();
            }
        } else {
            formContainer.setVisible(false);
            formContainer.setManaged(false);
            toggleFormBtn.setText("Ajouter un vol");
            currentFlight = null;
        }
    }

    private void resetForm() {
        codeField.clear();
        departureCombo.getSelectionModel().clearSelection();
        destinationCombo.getSelectionModel().clearSelection();
        departureDate.setValue(null);
        arrivalDate.setValue(null);
        statusCombo.getSelectionModel().clearSelection();
        aircraftCombo.getSelectionModel().clearSelection();
        crewCombo.getSelectionModel().clearSelection();
    }


    private void saveFlight() {
        System.out.println("Tentative d'enregistrement...");
        System.out.println("CurrentFlight: " + (currentFlight != null ? currentFlight.getCode() : "Nouveau vol"));
        // Validation des données
        if (codeField.getText().isEmpty() || departureCombo.getValue() == null ||
                destinationCombo.getValue() == null || departureDate.getValue() == null ||
                statusCombo.getValue() == null || aircraftCombo.getValue() == null ||
                crewCombo.getValue() == null) {

            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
            return;
        }

        LocalDate departure = departureDate.getValue();
        LocalDate arrival = arrivalDate.getValue();

        if (arrival != null && arrival.isBefore(departure)) {
            showAlert("Erreur", "La date d'arrivée ne peut pas être avant la date de départ", Alert.AlertType.ERROR);
            return;
        }

        // Création/mise à jour du vol
        Vol vol = new Vol(
                codeField.getText(),
                departureCombo.getValue(),
                destinationCombo.getValue(),
                Date.valueOf(departure),
                arrival != null ? Date.valueOf(arrival) : null,
                statusCombo.getValue(),
                crewCombo.getValue(),
                aircraftCombo.getValue(),
                false
        );

        boolean success;
        if (currentFlight == null) {
            // Ajout
            success = DaoVol.ajouter(vol);
        } else {
            // Modification
            success = DaoVol.modifier(vol);
        }


        if (success) {
            showAlert("Succès", currentFlight == null ? "Vol ajouté avec succès" : "Vol mis à jour avec succès",
                    Alert.AlertType.INFORMATION);
            loadFlights();
            toggleForm(false);
            System.out.println("Opération réussie!");

        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement", Alert.AlertType.ERROR);
            System.out.println("Échec de l'opération!");
        }
    }

    private void editFlight(Vol vol) {
        currentFlight = vol;

        codeField.setText(vol.getCode());
        departureCombo.setValue(vol.getLieuDepart());
        destinationCombo.setValue(vol.getDestination());
        departureDate.setValue(vol.getDateVol().toLocalDate());

        if (vol.getDateArrivee() != null) {
            arrivalDate.setValue(vol.getDateArrivee().toLocalDate());
        }

        statusCombo.setValue(vol.getStatut());
        aircraftCombo.setValue(vol.getAvion());
        crewCombo.setValue(vol.getEquipage());

        toggleForm(true);
    }

    private void deleteFlight(Vol vol) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le vol");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce vol ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (DaoVol.supprimer(vol)) {
                showAlert("Succès", "Vol supprimé avec succès", Alert.AlertType.INFORMATION);
                loadFlights();
            } else {
                showAlert("Erreur", "Échec de la suppression du vol", Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}