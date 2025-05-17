package DAO;

import Entity.Agent;

import java.sql.*;
import java.util.ArrayList;

public class DaoAgent {
    public static ArrayList<Agent> lister() {
    ArrayList<Agent> agents = new ArrayList<>();
    Connection cn = LaConnexion.seConnecter();
    String requete = "SELECT * FROM agentprogrammation";

    try {
        Statement st = cn.createStatement();
        ResultSet rs = st.executeQuery(requete);

        while (rs.next()) {
            String email = rs.getString(1); 
            String mdp = rs.getString(2);   
            Agent a = new Agent(email, mdp);
            agents.add(a);
        }
        System.out.println("Consultation réussie");
    } catch (SQLException e) {
        System.out.println("Problème de consultation : " + e.getMessage());
    }

    return agents;
}
    public static boolean ajouter(Agent a) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "insert into agentprogrammation values(?,?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getEmail());
            pst.setString(2, a.getMdp());
            

            
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
    public static boolean supprimer(Agent a) {
    Connection cn = LaConnexion.seConnecter();
    String requete = "delete from agentprogrammation where email=?;";
   
    try {
        PreparedStatement pst = cn.prepareStatement(requete);
        pst.setString(1, a.getEmail());

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
    public static boolean changerMdp(Agent a, String mdp) {
        Connection cn = LaConnexion.seConnecter();
        String requete = "update agentprogrammation set motDePasse='"+mdp+"' where email='"+a.getEmail()+"'";

        try {
            Statement st=cn.createStatement();
            int n=st.executeUpdate(requete);
            if (n >= 1) {
                System.out.println("Modif réussie");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête Modif : " + ex.getMessage());
        }

        return false;
    }

}
