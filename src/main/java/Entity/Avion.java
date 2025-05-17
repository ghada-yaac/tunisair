package Entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Avion {
    private static final Map<String, Integer> modeles = new HashMap<>();

    static {
        // Initialisation des modèles d'avions avec leurs capacités
        modeles.put("Boeing 737", 150);
        modeles.put("Boeing 747", 400);
        modeles.put("Airbus A320", 180);
        modeles.put("Airbus A350", 350);
        modeles.put("Embraer E190", 100);
    }

    private String matricule;
    private String modele;
    private int capacite;
    private TEtatAvion etat;
    private Date dateProchaineMaintenance;

    public Avion() {
    }

    public Avion(String matricule, String modele, int capacite, TEtatAvion etat, Date dateProchaineMaintenance) {
        this.matricule = matricule;
        this.modele = modele;
        this.capacite = capacite;
        this.etat = etat;
        this.dateProchaineMaintenance = dateProchaineMaintenance;
    }

    public static List<String> getModeles() {
        return new ArrayList<>(modeles.keySet());
    }

    public static int getCapacite(String modele) {
        return modeles.getOrDefault(modele, 0);
    }

    @Override
    public String toString() {
        return "Avion{" + "matricule=" + matricule + ", modele=" + modele + ", capacite=" + capacite + ", etat=" + etat
                + ", dateProchaineMaintenance=" + dateProchaineMaintenance + '}';
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public TEtatAvion getEtat() {
        return etat;
    }

    public void setEtat(TEtatAvion etat) {
        this.etat = etat;
    }

    public Date getDateProchaineMaintenance() {
        return dateProchaineMaintenance;
    }

    public void setDateProchaineMaintenance(Date dateProchaineMaintenance) {
        this.dateProchaineMaintenance = dateProchaineMaintenance;
    }
}
