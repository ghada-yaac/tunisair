package DAO;

import Entity.TStatut;
import Entity.Vol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoVol {

    public static ArrayList<Vol> lister() {
        ArrayList<Vol> vols = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();
        String requete = "SELECT * FROM vol";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            int i = 0;

            while (rs.next()) {
                i++;
                String code = rs.getString("code");
                String lieuDepart = rs.getString("lieuDepart");
                String destination = rs.getString("destination");
                Date dateVol = rs.getDate("dateDepart");
                TStatut statut = TStatut.valueOf(rs.getString("statut"));
                String equipage = rs.getString("equipage");
                Date ts = rs.getDate("dateArrivee");
                Date dateArrivee = ts != null ? new Date(ts.getTime()) : null;
                String avion = rs.getString("avion");
                boolean etatArchivage = rs.getBoolean("etatArchivage");

                Vol v = new Vol(code, lieuDepart, destination, dateVol, dateArrivee, statut, equipage, avion, etatArchivage);
                vols.add(v);
            }


            System.out.println("Consultation des vols réussie (" + i + " vols trouvés)");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des vols : " + e.getMessage());
        }

        return vols;
    }

    public static boolean ajouter(Vol v) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "INSERT INTO vol VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, v.getCode());
            pst.setString(2, v.getLieuDepart());
            pst.setString(3, v.getDestination());
            pst.setDate(4, v.getDateVol());
            pst.setString(5, v.getStatut().name());
            pst.setString(6, v.getEquipage());
            pst.setTimestamp(7, v.getDateArrivee() != null ? new Timestamp(v.getDateArrivee().getTime()) : null);
            pst.setString(8, v.getAvion());
            pst.setBoolean(9, v.isEtatArchivage());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Ajout du vol réussi");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur d'ajout du vol : " + ex.getMessage());
        }

        return false;
    }

    public static boolean supprimer(Vol v) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "DELETE FROM vol WHERE code = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, v.getCode());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Suppression du vol réussie");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du vol : " + ex.getMessage());
        }

        return false;
    }

    public static boolean modifier(Vol v) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "UPDATE vol SET lieuDepart = ?, destination = ?, dateDepart = ?, statut = ?, equipage = ?, dateArrivee = ?, avion = ?, etatArchivage = ? WHERE code = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, v.getLieuDepart());
            pst.setString(2, v.getDestination());
            pst.setDate(3, v.getDateVol());
            pst.setString(4, v.getStatut().name());
            pst.setString(5, v.getEquipage());
            pst.setTimestamp(6, v.getDateArrivee() != null ? new Timestamp(v.getDateArrivee().getTime()) : null);
            pst.setString(7, v.getAvion());
            pst.setBoolean(8, v.isEtatArchivage());
            pst.setString(9, v.getCode());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Modification du vol réussie");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du vol : " + ex.getMessage());
        }

        return false;
    }
    public static ArrayList<Vol> rechercher(String code, String lieuDepart, String destination,
                                            Date dateDepart, TStatut statut, String avion) {
        ArrayList<Vol> vols = new ArrayList<>();
        Connection cn = LaConnexion.seConnecter();

        StringBuilder requete = new StringBuilder("SELECT * FROM vol WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            requete.append(" AND code LIKE ?");
            params.add("%" + code + "%");
        }
        if (lieuDepart != null && !lieuDepart.isEmpty()) {
            requete.append(" AND lieuDepart = ?");
            params.add(lieuDepart);
        }
        if (destination != null && !destination.isEmpty()) {
            requete.append(" AND destination = ?");
            params.add(destination);
        }
        if (dateDepart != null) {
            requete.append(" AND dateDepart = ?");
            params.add(dateDepart);
        }
        if (statut != null) {
            requete.append(" AND statut = ?");
            params.add(statut.name());
        }
        if (avion != null && !avion.isEmpty()) {
            requete.append(" AND avion = ?");
            params.add(avion);
        }

        try {
            PreparedStatement pst = cn.prepareStatement(requete.toString());

            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String volCode = rs.getString("code");
                String volLieuDepart = rs.getString("lieuDepart");
                String volDestination = rs.getString("destination");
                Date volDateVol = rs.getDate("dateDepart");
                TStatut volStatut = TStatut.valueOf(rs.getString("statut"));
                String volEquipage = rs.getString("equipage");
                Date volDateArrivee = rs.getDate("dateArrivee");
                String volAvion = rs.getString("avion");
                boolean volEtatArchivage = rs.getBoolean("etatArchivage");

                Vol v = new Vol(volCode, volLieuDepart, volDestination, volDateVol,
                        volDateArrivee, volStatut, volEquipage, volAvion, volEtatArchivage);
                vols.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des vols : " + e.getMessage());
        }

        return vols;
    }
}
