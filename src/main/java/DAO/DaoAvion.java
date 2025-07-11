package DAO;

import Entity.Avion;
import Entity.TEtatAvion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoAvion {

    public static ArrayList<Avion> lister() {
        ArrayList<Avion> avions = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM avion WHERE archive = false";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String matricule = rs.getString(1);
                String modele = rs.getString(2);
                int capacite = rs.getInt(3);
                TEtatAvion etat = TEtatAvion.valueOf(rs.getString(4));
                Date date = rs.getDate(5);

                Avion a = new Avion(matricule, modele, capacite, etat, date, false);
                avions.add(a);
            }
            System.out.println("Consultation réussie");
        } catch (SQLException e) {
            System.out.println("Problème de consultation : " + e.getMessage());
        }

        return avions;
    }

    public static ArrayList<Avion> listerTous() {
        ArrayList<Avion> avions = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM avion";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String matricule = rs.getString(1);
                String modele = rs.getString(2);
                int capacite = rs.getInt(3);
                TEtatAvion etat = TEtatAvion.valueOf(rs.getString(4));
                Date date = rs.getDate(5);
                Boolean archive = rs.getBoolean(6);

                Avion a = new Avion(matricule, modele, capacite, etat, date, archive);
                avions.add(a);
            }
            System.out.println("Consultation réussie");
        } catch (SQLException e) {
            System.out.println("Problème de consultation : " + e.getMessage());
        }

        return avions;
    }

    public static boolean ajouter(Avion a) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "INSERT INTO avion VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getMatricule());
            pst.setString(2, a.getModele());
            pst.setInt(3, a.getCapacite());
            pst.setString(4, a.getEtat().name());
            pst.setDate(5, a.getDateProchaineMaintenance());
            pst.setBoolean(6, a.isArchive());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Ajout réussi");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête : " + ex.getMessage());
        }

        return false;
    }

    public static boolean archiver(Avion a) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "UPDATE avion SET archive = true WHERE matricule = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getMatricule());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Archivage réussi");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête d'archivage : " + ex.getMessage());
        }
        return false;
    }

    public static boolean modifier(Avion a) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "UPDATE avion SET modele=?, capacite=?, etat=?, dateProchaineMaintenance=?, archive=? WHERE matricule=?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getModele());
            pst.setInt(2, a.getCapacite());
            pst.setString(3, a.getEtat().name());
            pst.setDate(4, a.getDateProchaineMaintenance());
            pst.setDate(5, a.getDateProchaineMaintenance());
            pst.setString(6, a.getMatricule());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Modification réussie");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête de modification : " + ex.getMessage());
        }

        return false;
    }
}
