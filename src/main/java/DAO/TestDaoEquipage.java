package DAO;

import Entity.Equipage;
import Entity.Vol;
import DAO.DaoEquipage;
import java.util.ArrayList;

public class TestDaoEquipage {
    public static void main(String[] args) {
                // Créer un objet Equipage avec un code existant dans la base
                Equipage e = new Equipage();
                e.setCode("2"); // ⚠️ Remplace par un code valide présent en BDD

        DaoEquipage volDAO = new DaoEquipage(); // Classe contenant ta méthode
                ArrayList<Vol> vols = volDAO.getVolParEquipage(e);

                // Vérification et affichage des résultats
                if (vols.isEmpty()) {
                    System.out.println("Aucun vol trouvé pour l'équipage " + e.getCode());
                } else {
                    System.out.println("Vols trouvés pour l'équipage " + e.getCode() + " :");
                    for (Vol v : vols) {
                        System.out.println("Code Vol        : " + v.getCode());
                        System.out.println("Lieu Départ     : " + v.getLieuDepart());
                        System.out.println("Destination     : " + v.getDestination());
                        System.out.println("Date Vol        : " + v.getDateVol());
                        System.out.println("Date Arrivée    : " + v.getDateArrivee());
                        System.out.println("Statut          : " + v.getStatut());
                        System.out.println("Code Équipage   : " + v.getEquipage());
                        System.out.println("Avion           : " + v.getAvion());
                        System.out.println("Archivé         : " + v.isEtatArchivage());
                        System.out.println("---------------------------");
                    }
                }
            }
        }


