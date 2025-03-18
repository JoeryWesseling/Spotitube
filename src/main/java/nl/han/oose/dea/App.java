package nl.han.oose.dea;

import nl.han.oose.dea.data.database.DatabaseConnection;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection()) {
            System.out.println("Verbonden met MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
