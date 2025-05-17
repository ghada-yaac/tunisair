package DAO;

import Entity.Agent;

import java.util.ArrayList;

public class TestDaoAgent {
    public static void main(String[] args) {
        LaConnexion.setUser("root");
        LaConnexion.setPassWord(""); 
        LaConnexion.seConnecter();
        Agent a1=new Agent("aminekilani901@gmail.com","123");
        Agent a2=new Agent("ghadayaacoubi55@gmail.com","321");
        DaoAgent.ajouter(a1);
        DaoAgent.ajouter(a2);

        
        DaoAgent.changerMdp(a1, "321");
        ArrayList<Agent>ag2=DaoAgent.lister();
        System.out.println(ag2);
        
        
    }
}
