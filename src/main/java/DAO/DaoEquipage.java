package DAO;

import Entity.Equipage;

import java.sql.*;
import java.util.ArrayList;

public class DaoEquipage {

    public static ArrayList<Equipage> lister() {
        ArrayList<Equipage> equipages = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM equipage";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                String code = rs.getString(1);
                String pilote = rs.getString(2);
                String copilote = rs.getString(3);
                String hotesse1 = rs.getString(4);
                String hotesse2 = rs.getString(5);
                String hotesse3 = rs.getString(6);

                Equipage e = new Equipage(code, pilote, copilote, hotesse1, hotesse2, hotesse3);
                equipages.add(e);
            }
            System.out.println("Consultation réussie");
        } catch (SQLException e) {
            System.out.println("Problème de consultation : " + e.getMessage());
        }

        return equipages;
    }

    public static boolean ajouter(Equipage e) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "INSERT INTO equipage (code, pilote, copilote, hotesse1, hotesse2, hotesse3) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, e.getCode());
            pst.setString(2, e.getPilote());
            pst.setString(3, e.getCopilote());
            pst.setString(4, e.getHotesses1());
            pst.setString(5, e.getHotesse2());
            pst.setString(6, e.getHotesse3());

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

    public static boolean supprimer(Equipage e) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "DELETE FROM equipage WHERE code=?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, e.getCode());

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

    public static boolean modifier(Equipage e) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "UPDATE equipage SET pilote=?, copilote=?, hotesse1=?, hotesse2=?, hotesse3=? WHERE code=?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, e.getPilote());
            pst.setString(2, e.getCopilote());
            pst.setString(3, e.getHotesses1());
            pst.setString(4, e.getHotesse2());
            pst.setString(5, e.getHotesse3());
            pst.setString(6, e.getCode());

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
