package DAO;

import Entity.TStatut;
import Entity.Vol;

import java.sql.Date;
import java.util.Calendar;

public class TestDaoVol {
    public static void main(String[] args) {
        // Création de quelques dates
        Date dateVol1 = new Date(Calendar.getInstance().getTimeInMillis());
        Date dateArrivee1 = new Date(dateVol1.getTime() + 2 * 60 * 60 * 1000); // +2 heures

        // Vol 1
        Vol v1 = new Vol("V001", "Tunis", "Paris", dateVol1, dateArrivee1, TStatut.confirme, "E001", "ABC100", false);
        //DaoVol.ajouter(v1);

        // Vol 2
        Vol v2 = new Vol("V002", "Sfax", "Rome", dateVol1, null, TStatut.enAttente, "E001", "ABC100", false);
        //DaoVol.ajouter(v2);

        // Vol 3
        Vol v3 = new Vol("V003", "Djerba", "Madrid", dateVol1, dateArrivee1, TStatut.annule, "E001", "ABC100", true);
        //DaoVol.ajouter(v3);

        // Vol 4
        Vol v4 = new Vol("V004", "Monastir", "Berlin", dateVol1, dateArrivee1, TStatut.confirme, "2", "ABC100", false);
        DaoVol.ajouter(v4);

        // Vol 5
        Vol v5 = new Vol("V005", "Tunis", "Lisbonne", dateVol1, dateArrivee1, TStatut.enAttente, "2", "ABC10", false);
//        DaoVol.ajouter(v5);
        System.out.println(DaoAgent.lister());
    }
}
