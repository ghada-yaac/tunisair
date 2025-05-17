package DAO;

import Entity.Personnel;

public class TestDaoPersonnel {
    public static void main(String[] args) {
        System.out.println("\nListe des pilotes :");
        for (Personnel pilote : DaoPersonnel.getTousLesPilotes()) {
            System.out.println(pilote.getNom() + " - " + pilote.getEmail());
        }

        System.out.println("\nListe des hôtesses :");
        for (Personnel hotesse : DaoPersonnel.getToutesLesHotesses()) {
            System.out.println(hotesse.getNom() + " - " + hotesse.getEmail());
        }

    }
}