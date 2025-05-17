package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LaConnexion {
    private static Connection con = null;
    private static String user = "root";
    private static String passWord = "";

    public static void setUser(String u){
        user = u;
    }

    public static void setPassWord(String pw){
        passWord = pw;
    }

    public static Connection seConnecter(){
        if (con == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunisair", user, passWord);
                System.out.println("Connexion établie");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver non trouvé : " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("BD non trouvée ou problème d'identification : " + ex.getMessage());
            }
        }
        return con;
    }
}
