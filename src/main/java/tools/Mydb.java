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

            // Table des jeux de base
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS games (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    TypeJeux VARCHAR(100) NOT NULL,
                    difficulte VARCHAR(50),
                    time_limit INT,
                    ScoreMax INT
                )
            """);

            // Table des Battles (Espaces de compétition)
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS battle (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    battle_type VARCHAR(50) DEFAULT 'duel', -- duel, team, ia
                    status VARCHAR(50) DEFAULT 'waiting',   -- waiting, ongoing, finished
                    start_time DATETIME,
                    end_time DATETIME,
                    gagnant VARCHAR(100)
                )
            """);

            // Table des Joueurs (Participants à une battle)
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS joueur (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    battle_id INT,
                    user_id INT,
                    score INT DEFAULT 0,
                    rank INT DEFAULT 0,
                    status VARCHAR(50) DEFAULT 'active', -- active, eliminated, winner
                    FOREIGN KEY (battle_id) REFERENCES battle(id) ON DELETE CASCADE
                )
            """);

            // Table des parties (Historique)
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS partie (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    score INT,
                    datee DATE,
                    joueur_id INT,
                    FOREIGN KEY (joueur_id) REFERENCES joueur(id)
                )
            """);

            // Mise à jour de la table battle si elle existait déjà
            try { st.executeUpdate("ALTER TABLE battle ADD COLUMN battle_type VARCHAR(50) DEFAULT 'duel'"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE battle ADD COLUMN status VARCHAR(50) DEFAULT 'waiting'"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE battle ADD COLUMN start_time DATETIME"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE battle ADD COLUMN end_time DATETIME"); } catch (Exception e) {}

            // Mise à jour de la table joueur si elle existait déjà
            try { st.executeUpdate("ALTER TABLE joueur ADD COLUMN battle_id INT"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE joueur ADD COLUMN user_id INT"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE joueur ADD COLUMN score INT DEFAULT 0"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE joueur ADD COLUMN rank INT DEFAULT 0"); } catch (Exception e) {}
            try { st.executeUpdate("ALTER TABLE joueur ADD COLUMN status VARCHAR(50) DEFAULT 'active'"); } catch (Exception e) {}

            System.out.println("Schéma de base de données synchronisé !");

        } catch (Exception e) {
            System.out.println("Erreur migration tables : " + e.getMessage());
        }
    }
}