package Controller;

import DAO.DaoPersonnel;
import Entity.Personnel;
import Entity.TRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PersonnelController {

    @FXML
    private TextField searchField;

    @FXML
    private Button addPersonnelBtn;

    @FXML
    private Label totalPersonnelCount;

    @FXML
    private Label pilotesCount;

    @FXML
    private Label hotessesCount;

    @FXML
    private VBox formContainer;

    @FXML
    private Label formTitle;

    @FXML
    private TextField matriculeField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<TRole> roleComboBox;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private Button annulerBtn;

    @FXML
    private TableView<Personnel> personnelTable;

    @FXML
    private TableColumn<Personnel, String> matriculeColumn;

    @FXML
    private TableColumn<Personnel, String> nomColumn;

    @FXML
    private TableColumn<Personnel, String> emailColumn;

    @FXML
    private TableColumn<Personnel, TRole> roleColumn;

    @FXML
    private TableColumn<Personnel, Void> actionColumn;

    private ObservableList<Personnel> personnelList;
    private FilteredList<Personnel> filteredPersonnelList;
    private Personnel selectedPersonnel;

    @FXML
    public void initialize() {
        // Initialize table columns
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Initialize personnel list
        personnelList = FXCollections.observableArrayList(DaoPersonnel.lister());
        filteredPersonnelList = new FilteredList<>(personnelList, p -> true);
        personnelTable.setItems(filteredPersonnelList);

        // Initialize role combo box
        roleComboBox.setItems(FXCollections.observableArrayList(TRole.values()));

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPersonnelList.setPredicate(personnel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return personnel.getMatricule().toLowerCase().contains(lowerCaseFilter) ||
                        personnel.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        personnel.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                        personnel.getRole().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Setup action column with edit and delete buttons
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> {
                    Personnel personnel = getTableView().getItems().get(getIndex());
                    showFormForEdit(personnel);
                });

                deleteButton.setOnAction(event -> {
                    Personnel personnel = getTableView().getItems().get(getIndex());
                    handleDelete(personnel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });

        // Update statistics
        updateStatistics();

        // Add button action
        addPersonnelBtn.setOnAction(event -> showFormForAdd());
    }

    private void updateStatistics() {
        totalPersonnelCount.setText(String.valueOf(DaoPersonnel.lister().size()));
        pilotesCount.setText(String.valueOf(DaoPersonnel.getTousLesPilotes().size()));
        hotessesCount.setText(String.valueOf(DaoPersonnel.getToutesLesHotesses().size()));
    }

    private void showFormForAdd() {
        formTitle.setText("Ajouter un membre");
        matriculeField.setText("");
        nomField.setText("");
        emailField.setText("");
        roleComboBox.getSelectionModel().clearSelection();
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedPersonnel = null;
    }

    private void showFormForEdit(Personnel personnel) {
        formTitle.setText("Modifier un membre");
        matriculeField.setText(personnel.getMatricule());
        nomField.setText(personnel.getNom());
        emailField.setText(personnel.getEmail());
        roleComboBox.getSelectionModel().select(personnel.getRole());
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedPersonnel = personnel;
    }

    @FXML
    private void handleSave() {
        String matricule = matriculeField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        TRole role = roleComboBox.getValue();

        if (matricule.isEmpty() || nom.isEmpty() || email.isEmpty() || role == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Personnel personnel = new Personnel(matricule, nom, email, role);

        if (selectedPersonnel == null) {
            // Add new personnel
            if (DaoPersonnel.ajouter(personnel)) {
                personnelList.add(personnel);
                hideForm();
                updateStatistics();
            } else {
                showAlert("Erreur", "Échec de l'ajout du personnel.");
            }
        } else {
            // Update existing personnel
            if (DaoPersonnel.supprimer(selectedPersonnel) && DaoPersonnel.ajouter(personnel)) {
                personnelList.remove(selectedPersonnel);
                personnelList.add(personnel);
                hideForm();
                updateStatistics();
            } else {
                showAlert("Erreur", "Échec de la modification du personnel.");
            }
        }
    }

    @FXML
    private void hideForm() {
        formContainer.setVisible(false);
        formContainer.setManaged(false);
        selectedPersonnel = null;
    }

    private void handleDelete(Personnel personnel) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Voulez-vous vraiment supprimer ce membre ?");
        confirm.setContentText("Matricule: " + personnel.getMatricule() + "\nNom: " + personnel.getNom());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (DaoPersonnel.supprimer(personnel)) {
                personnelList.remove(personnel);
                updateStatistics();
            } else {
                showAlert("Erreur", "Échec de la suppression du personnel.");
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

