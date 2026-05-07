package services;

import entities.Games;
import tools.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceGames implements IService<Games> {

    private Connection connection = Mydb.getInstance().getConnection();

    @Override
    public void ajouter(Games g) {
        try {
            String sql = "INSERT INTO games(TypeJeux, difficulte, time_limit, ScoreMax) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, g.getTypeJeux());
            ps.setString(2, g.getDifficulte());
            ps.setInt(3, g.getTimeLimit());
            ps.setInt(4, g.getScoreMax());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Games g) {
        try {
            String sql = "UPDATE games SET TypeJeux=?, difficulte=?, time_limit=?, ScoreMax=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, g.getTypeJeux());
            ps.setString(2, g.getDifficulte());
            ps.setInt(3, g.getTimeLimit());
            ps.setInt(4, g.getScoreMax());
            ps.setInt(5, g.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Games g) {
        try {
            String sql = "DELETE FROM games WHERE id= ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, g.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Games> getAll() {
        List<Games> games = new ArrayList<>();
        try {
            String sql = "SELECT * FROM games";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                games.add(new Games(
                        rs.getInt("id"),
                        rs.getString("TypeJeux"),
                        rs.getString("difficulte"),
                        rs.getInt("time_limit"),
                        rs.getInt("ScoreMax")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return games;
    }

    @Override
    public Games getById(int id) {
        try {
            String sql = "SELECT * FROM games WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Games(
                        rs.getInt("id"),
                        rs.getString("TypeJeux"),
                        rs.getString("difficulte"),
                        rs.getInt("time_limit"),
                        rs.getInt("ScoreMax")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}