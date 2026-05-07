package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Gestionnaire de connexion Singleton pour MySQL.
 * Assure une instance unique de connexion pour toute l'application.
 */
public class Mydb {

    private static Mydb instance;
    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/workshop";
    private final String user = "root";
    private final String pass = "";

    private Mydb() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected !");
            creerTables();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Mydb getInstance() {
        if (instance == null) {
            instance = new Mydb();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void creerTables() {
        try {
            Statement st = connection.createStatement();

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS games (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    TypeJeux VARCHAR(100) NOT NULL,
                    difficulte VARCHAR(50),
                    time_limit INT,
                    ScoreMax INT
                )
            """);

            try { st.executeUpdate("ALTER TABLE games ADD COLUMN difficulte VARCHAR(50)"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE games ADD COLUMN time_limit INT"); } catch (Exception e) {}

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS battle (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    scoreJoueur1 INT,
                    scoreJoueur2 INT,
                    gagnant VARCHAR(100)
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS joueur (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    points INT
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS partie (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    score INT,
                    datee DATE,
                    joueur_id INT,
                    FOREIGN KEY (joueur_id) REFERENCES joueur(id)
                )
            """);

            System.out.println("Tables créées avec succès !");

        } catch (Exception e) {
            System.out.println("Erreur création tables : " + e.getMessage());
        }
    }
}