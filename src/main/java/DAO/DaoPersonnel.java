package DAO;

import Entity.Personnel;
import Entity.TRole;

import java.sql.*;
import java.util.ArrayList;


public class DaoPersonnel {
public static ArrayList<Personnel> lister() {
        ArrayList<Personnel> personnels = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM personnel";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String matricule = rs.getString(1);
                String nom = rs.getString(2);
                String email = rs.getString(3);
                TRole role = TRole.valueOf(rs.getString(4)); 

                Personnel p = new Personnel(matricule, nom, email, role);
                personnels.add(p);
            }
            System.out.println("Consultation réussie");
        } catch (SQLException e) {
            System.out.println("Problème de consultation : " + e.getMessage());
        }

        return personnels;
    }

    public static boolean ajouter(Personnel p) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "INSERT INTO personnel VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, p.getMatricule());
            pst.setString(2, p.getNom());
            pst.setString(3, p.getEmail());
            pst.setString(4, p.getRole().name()); 

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

    public static boolean supprimer(Personnel p) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "DELETE FROM personnel WHERE matricule = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, p.getMatricule());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Suppression réussie");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête de suppression : " + ex.getMessage());
        }

        return false;
    }
    public static ArrayList<Personnel> getTousLesPilotes() {
        ArrayList<Personnel> pilotes = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM personnel WHERE role = 'pilote'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String code = rs.getString("matricule");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                TRole role = TRole.valueOf(rs.getString("role"));

                Personnel p = new Personnel(code, nom, email, role);
                pilotes.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des pilotes : " + e.getMessage());
        }

        return pilotes;
    }

    public static ArrayList<Personnel> getToutesLesHotesses() {
        ArrayList<Personnel> hotesses = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM personnel WHERE role = 'hotesse'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String code = rs.getString("matricule");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                TRole role = TRole.valueOf(rs.getString("role"));

                Personnel p = new Personnel(code, nom, email, role);
                hotesses.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des hôtesses : " + e.getMessage());
        }

        return hotesses;
    }

}
