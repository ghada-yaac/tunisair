package DAO;

import Entity.Avion;
import Entity.TEtatAvion;

import java.sql.Date;
import java.util.ArrayList;


public class TestDaoAvion {

    public static void main(String[] args) {
         Avion avionTest = new Avion(
            "A",
            "Boeing 737",                
            100,                        
            TEtatAvion.disponible,            
            Date.valueOf("2025-12-31"),
            false
        );
         DaoAvion.ajouter(avionTest);
      ArrayList <Avion>av=DaoAvion.lister();
        System.out.println(av);
        
        
}}