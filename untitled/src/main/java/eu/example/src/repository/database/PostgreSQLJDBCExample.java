package eu.example.src.repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLJDBCExample {
    public static void main(String[] args) throws ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5432/socialnetwork"; // Numele bazei de date
        String user = "vasilegeorge"; // Numele utilizatorului
        String password = "parola"; // Parola utilizatorului
        Class.forName("org.postgresql.Driver");


        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conectat la baza de date!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
