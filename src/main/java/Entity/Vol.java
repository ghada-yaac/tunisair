package Entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class Vol {
    private static final List<String> villes = Arrays.asList(
            "Tunis",
            "Paris",
            "Lyon",
            "Marseille",
            "Rome",
            "Milan",
            "Istanbul",
            "Dubai",
            "Le Caire",
            "Casablanca",
            "Alger",
            "Barcelone",
            "Madrid",
            "Londres",
            "Francfort",
            "Amsterdam",
            "Bruxelles",
            "Genève",
            "Vienne",
            "Athènes");

    private StringProperty code;
    private StringProperty lieuDepart;
    private StringProperty destination;
    private ObjectProperty<Date> dateVol;
    private ObjectProperty<Date> dateArrivee;
    private ObjectProperty<TStatut> statut;
    private StringProperty equipage;
    private StringProperty avion;
    private boolean etatArchivage = false;

    public Vol() {
        this.code = new SimpleStringProperty();
        this.lieuDepart = new SimpleStringProperty();
        this.destination = new SimpleStringProperty();
        this.dateVol = new SimpleObjectProperty<>();
        this.statut = new SimpleObjectProperty<>();
        this.equipage = new SimpleStringProperty();
    }

    public Vol(String code, String lieuDepart, String destination, Date dateVol, Date dateArrivee, TStatut statut, String equipage, String avion, boolean etatArchivage) {
        this.code = new SimpleStringProperty(code);
        this.lieuDepart = new SimpleStringProperty(lieuDepart);
        this.destination = new SimpleStringProperty(destination);
        this.dateVol = new SimpleObjectProperty<>(dateVol);
        this.dateArrivee = new SimpleObjectProperty<>(dateArrivee);
        this.statut = new SimpleObjectProperty<>(statut);
        this.equipage = new SimpleStringProperty(equipage);
        this.avion = new SimpleStringProperty(avion);
        this.etatArchivage = etatArchivage;
    }

    public Vol(String code, String departure, String destination, Date date, TStatut status, String crew, boolean b) {
    }

    public StringProperty avionProperty() {
        if (avion == null) {
            avion = new SimpleStringProperty();
        }
        return avion;
    }

    public String getAvion() {
        return avion.get();
    }

    public void setAvion(String avion) {
        this.avion.set(avion);
    }

    // Getters et setters pour dateArrivee
    public ObjectProperty<Date> dateArriveeProperty() {
        if (dateArrivee == null) {
            dateArrivee = new SimpleObjectProperty<>();
        }
        return dateArrivee;
    }

    public Date getDateArrivee() {
        return dateArrivee.get();
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee.set(dateArrivee);
    }

    // Getters et setters pour etatArchivage
    public boolean isEtatArchivage() {
        return etatArchivage;
    }

    public void setEtatArchivage(boolean etatArchivage) {
        this.etatArchivage = etatArchivage;
    }
    public static List<String> getVilles() {
        return villes;
    }

    // Getters et setters pour les propriétés
    public StringProperty codeProperty() {
        return code;
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty lieuDepartProperty() {
        return lieuDepart;
    }

    public String getLieuDepart() {
        return lieuDepart.get();
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart.set(lieuDepart);
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public String getDestination() {
        return destination.get();
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public ObjectProperty<Date> dateVolProperty() {
        return dateVol;
    }

    public Date getDateVol() {
        return dateVol.get();
    }

    public void setDateVol(Date dateVol) {
        this.dateVol.set(dateVol);
    }

    public ObjectProperty<TStatut> statutProperty() {
        return statut;
    }

    public TStatut getStatut() {
        return statut.get();
    }

    public void setStatut(TStatut statut) {
        this.statut.set(statut);
    }

    public StringProperty equipageProperty() {
        return equipage;
    }

    public String getEquipage() {
        return equipage.get();
    }

    public void setEquipage(String equipage) {
        this.equipage.set(equipage);
    }
}
