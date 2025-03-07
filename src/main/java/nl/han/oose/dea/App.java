package nl.han.oose.dea;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import nl.han.oose.dea.data.DAO.DatabaseConnection;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection()) {
            System.out.println("✅ Verbonden met MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
