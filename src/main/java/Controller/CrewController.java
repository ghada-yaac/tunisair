package Controller;

import DAO.DaoEquipage;
import DAO.DaoPersonnel;
import Entity.Equipage;
import Entity.Personnel;
import Entity.TRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CrewController {

    @FXML
    private Button addCrewBtn;

    @FXML
    private TableView<Equipage> crewsTable;

    @FXML
    private TableColumn<Equipage, String> idColumn;

    @FXML
    private TableColumn<Equipage, String> piloteColumn;

    @FXML
    private TableColumn<Equipage, String> copiloteColumn;

    @FXML
    private TableColumn<Equipage, String> hotessesColumn;

    @FXML
    private TableColumn<Equipage, Void> actionsColumn;

    @FXML
    private VBox formContainer;

    @FXML
    private Label formTitle;

    @FXML
    private TextField codeField;

    @FXML
    private ComboBox<Personnel> piloteComboBox;

    @FXML
    private ComboBox<Personnel> copiloteComboBox;

    @FXML
    private ComboBox<Personnel> hotesse1ComboBox;

    @FXML
    private ComboBox<Personnel> hotesse2ComboBox;

    @FXML
    private ComboBox<Personnel> hotesse3ComboBox;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private Button annulerBtn;

    private ObservableList<Equipage> crewList;
    private ObservableList<Personnel> availablePilots;
    private ObservableList<Personnel> availableHotesses;
    private Equipage selectedCrew;

    @FXML
    public void initialize() {
        // Initialize table columns with custom CellValueFactory to avoid reflection issues
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        piloteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPilote()));
        copiloteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCopilote()));
        hotessesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.join(", ",
                        cellData.getValue().getHotesses1() != null ? cellData.getValue().getHotesses1() : "",
                        cellData.getValue().getHotesse2() != null ? cellData.getValue().getHotesse2() : "",
                        cellData.getValue().getHotesse3() != null ? cellData.getValue().getHotesse3() : ""
                ).replaceAll(",\\s*,", ",").replaceAll("^,|,$", "").trim()
        ));

        // Initialize crew list
        crewList = FXCollections.observableArrayList(DaoEquipage.lister());
        crewsTable.setItems(crewList);

        // Debug: Print loaded crews
        crewList.forEach(e -> System.out.println("Crew: " + e.getCode() + ", " + e.getPilote() + ", " +
                e.getCopilote() + ", " + e.getHotesses1() + ", " + e.getHotesse2() + ", " + e.getHotesse3()));

        // Initialize ComboBoxes with available personnel
        updateAvailablePersonnel(null);

        // Setup ComboBox cell factories to display matricule
        piloteComboBox.setCellFactory(listView -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });
        piloteComboBox.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });

        copiloteComboBox.setCellFactory(listView -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });
        copiloteComboBox.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });

        hotesse1ComboBox.setCellFactory(listView -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });
        hotesse1ComboBox.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });

        hotesse2ComboBox.setCellFactory(listView -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });
        hotesse2ComboBox.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });

        hotesse3ComboBox.setCellFactory(listView -> new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });
        hotesse3ComboBox.setButtonCell(new ListCell<Personnel>() {
            @Override
            protected void updateItem(Personnel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getMatricule());
            }
        });

        // Setup action column with edit and delete buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> {
                    Equipage crew = getTableView().getItems().get(getIndex());
                    showFormForEdit(crew);
                });

                deleteButton.setOnAction(event -> {
                    Equipage crew = getTableView().getItems().get(getIndex());
                    handleDelete(crew);
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

        // Add button action
        addCrewBtn.setOnAction(event -> showFormForAdd());

        // Initialize form as hidden
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private void updateAvailablePersonnel(Equipage excludeCrew) {
        // Get all crews to find used personnel, excluding the crew being edited
        Set<String> usedMatricules = new HashSet<>();
        crewList.forEach(crew -> {
            if (excludeCrew != null && crew.getCode().equals(excludeCrew.getCode())) {
                return; // Skip the crew being edited
            }
            if (crew.getPilote() != null) usedMatricules.add(crew.getPilote());
            if (crew.getCopilote() != null) usedMatricules.add(crew.getCopilote());
            if (crew.getHotesses1() != null) usedMatricules.add(crew.getHotesses1());
            if (crew.getHotesse2() != null) usedMatricules.add(crew.getHotesse2());
            if (crew.getHotesse3() != null) usedMatricules.add(crew.getHotesse3());
        });

        // Get available pilots and hostesses
        availablePilots = FXCollections.observableArrayList(
                DaoPersonnel.getTousLesPilotes().stream()
                        .filter(p -> !usedMatricules.contains(p.getMatricule()))
                        .collect(Collectors.toList())
        );
        availableHotesses = FXCollections.observableArrayList(
                DaoPersonnel.getToutesLesHotesses().stream()
                        .filter(p -> !usedMatricules.contains(p.getMatricule()))
                        .collect(Collectors.toList())
        );

        // Add the current crew's personnel to the ComboBox options if editing
        if (excludeCrew != null) {
            ArrayList<Personnel> allPilots = DaoPersonnel.getTousLesPilotes();
            ArrayList<Personnel> allHotesses = DaoPersonnel.getToutesLesHotesses();

            if (excludeCrew.getPilote() != null) {
                allPilots.stream()
                        .filter(p -> p.getMatricule().equals(excludeCrew.getPilote()))
                        .findFirst()
                        .ifPresent(availablePilots::add);
            }
            if (excludeCrew.getCopilote() != null) {
                allPilots.stream()
                        .filter(p -> p.getMatricule().equals(excludeCrew.getCopilote()))
                        .findFirst()
                        .ifPresent(availablePilots::add);
            }
            if (excludeCrew.getHotesses1() != null) {
                allHotesses.stream()
                        .filter(p -> p.getMatricule().equals(excludeCrew.getHotesses1()))
                        .findFirst()
                        .ifPresent(availableHotesses::add);
            }
            if (excludeCrew.getHotesse2() != null) {
                allHotesses.stream()
                        .filter(p -> p.getMatricule().equals(excludeCrew.getHotesse2()))
                        .findFirst()
                        .ifPresent(availableHotesses::add);
            }
            if (excludeCrew.getHotesse3() != null) {
                allHotesses.stream()
                        .filter(p -> p.getMatricule().equals(excludeCrew.getHotesse3()))
                        .findFirst()
                        .ifPresent(availableHotesses::add);
            }
        }

        // Update ComboBoxes
        piloteComboBox.setItems(availablePilots);
        copiloteComboBox.setItems(availablePilots);
        hotesse1ComboBox.setItems(availableHotesses);
        hotesse2ComboBox.setItems(availableHotesses);
        hotesse3ComboBox.setItems(availableHotesses);

        // Debug: Print available personnel
        System.out.println("Available Pilots: " + availablePilots);
        System.out.println("Available Hotesses: " + availableHotesses);
    }

    private void showFormForAdd() {
        formTitle.setText("Ajouter un équipage");
        codeField.setText("");
        piloteComboBox.getSelectionModel().clearSelection();
        copiloteComboBox.getSelectionModel().clearSelection();
        hotesse1ComboBox.getSelectionModel().clearSelection();
        hotesse2ComboBox.getSelectionModel().clearSelection();
        hotesse3ComboBox.getSelectionModel().clearSelection();
        updateAvailablePersonnel(null);
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedCrew = null;
    }

    private void showFormForEdit(Equipage crew) {
        formTitle.setText("Modifier un équipage");
        codeField.setText(crew.getCode());

        // Update available personnel, excluding the current crew
        updateAvailablePersonnel(crew);

        // Debug: Print crew data
        System.out.println("Editing Crew: " + crew.getCode() + ", Pilote: " + crew.getPilote() +
                ", Copilote: " + crew.getCopilote() + ", Hotesse1: " + crew.getHotesses1() +
                ", Hotesse2: " + crew.getHotesse2() + ", Hotesse3: " + crew.getHotesse3());

        // Find personnel by matricule
        piloteComboBox.getSelectionModel().select(
                availablePilots.stream().filter(p -> p.getMatricule().equals(crew.getPilote())).findFirst().orElse(null)
        );
        copiloteComboBox.getSelectionModel().select(
                availablePilots.stream().filter(p -> p.getMatricule().equals(crew.getCopilote())).findFirst().orElse(null)
        );
        hotesse1ComboBox.getSelectionModel().select(
                availableHotesses.stream().filter(p -> p.getMatricule().equals(crew.getHotesses1())).findFirst().orElse(null)
        );
        hotesse2ComboBox.getSelectionModel().select(
                availableHotesses.stream().filter(p -> p.getMatricule().equals(crew.getHotesse2())).findFirst().orElse(null)
        );
        hotesse3ComboBox.getSelectionModel().select(
                availableHotesses.stream().filter(p -> p.getMatricule().equals(crew.getHotesse3())).findFirst().orElse(null)
        );

        formContainer.setVisible(true);
        formContainer.setManaged(true);
        selectedCrew = crew;
    }

    @FXML
    private void handleSave() {
        String code = codeField.getText();
        Personnel pilote = piloteComboBox.getValue();
        Personnel copilote = copiloteComboBox.getValue();
        Personnel hotesse1 = hotesse1ComboBox.getValue();
        Personnel hotesse2 = hotesse2ComboBox.getValue();
        Personnel hotesse3 = hotesse3ComboBox.getValue();

        if (code.isEmpty() || pilote == null || copilote == null) {
            showAlert("Erreur", "Les champs Code, Pilote et Copilote sont obligatoires.");
            return;
        }

        if (pilote.equals(copilote)) {
            showAlert("Erreur", "Le pilote et le copilote doivent être différents.");
            return;
        }

        Equipage crew = new Equipage(
                code,
                pilote.getMatricule(),
                copilote.getMatricule(),
                hotesse1 != null ? hotesse1.getMatricule() : null,
                hotesse2 != null ? hotesse2.getMatricule() : null,
                hotesse3 != null ? hotesse3.getMatricule() : null
        );

        if (selectedCrew == null) {
            // Add new crew
            if (DaoEquipage.ajouter(crew)) {
                crewList.add(crew);
                hideForm();
                updateAvailablePersonnel(null);
            } else {
                showAlert("Erreur", "Échec de l'ajout de l'équipage.");
            }
        } else {
            // Update existing crew
            if (DaoEquipage.modifier(crew)) {
                crewList.remove(selectedCrew);
                crewList.add(crew);
                hideForm();
                updateAvailablePersonnel(null);
            } else {
                showAlert("Erreur", "Échec de la modification de l'équipage.");
            }
        }
        crewsTable.refresh();
    }

    @FXML
    private void hideForm() {
        formContainer.setVisible(false);
        formContainer.setManaged(false);
        selectedCrew = null;
    }

    private void handleDelete(Equipage crew) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Voulez-vous vraiment supprimer cet équipage ?");
        confirm.setContentText("Code: " + crew.getCode());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (DaoEquipage.supprimer(crew)) {
                crewList.remove(crew);
                updateAvailablePersonnel(null);
            } else {
                showAlert("Erreur", "Échec de la suppression de l'équipage.");
            }
        }
        crewsTable.refresh();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}