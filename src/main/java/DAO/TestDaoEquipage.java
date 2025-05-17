package DAO;

import Entity.Equipage;

import java.util.ArrayList;

public class TestDaoEquipage {
    public static void main(String[] args) {
        // 1. Insertion de deux équipages
        Equipage e1 = new Equipage("E001", "P001", "P002", "P004", "P005", "P004");
        Equipage e2 = new Equipage("E002", "P001", "P002", "P004", "P005", "P004");

        System.out.println("Insertion des équipages...");
        if (DaoEquipage.ajouter(e1)) {
            System.out.println("Equipage E001 ajouté");
        }
        if (DaoEquipage.ajouter(e2)) {
            System.out.println("Equipage E002 ajouté");
        }

        // Affichage de la liste après insertion
        System.out.println("\nListe après insertion :");
        ArrayList<Equipage> listeEquipages = DaoEquipage.lister();
        for (Equipage e : listeEquipages) {
            System.out.println(e);
        }

        // 2. Modification de l'équipage e1 (changement du pilote et du copilote par exemple)
        System.out.println("\nModification de l'équipage E001...");
        e1.setPilote("Pilote1Modifie");
        e1.setCopilote("Copilote1Modifie");

        if (DaoEquipage.modifier(e1)) {
            System.out.println("Equipage E001 modifié");
        }

        // Affichage de la liste après modification
        System.out.println("\nListe après modification :");
        listeEquipages = DaoEquipage.lister();
        for (Equipage e : listeEquipages) {
            System.out.println(e);
        }

        // 3. Suppression de l'équipage e2
        System.out.println("\nSuppression de l'équipage E002...");
        if (DaoEquipage.supprimer(e2)) {
            System.out.println("Equipage E002 supprimé");
        }

        // Affichage final de la liste après suppression
        System.out.println("\nListe finale :");
        listeEquipages = DaoEquipage.lister();
        for (Equipage e : listeEquipages) {
            System.out.println(e);
        }
    }
}
