package services;

import entities.Battle;
import tools.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ServiceBattle implements IService<Battle> {

    private Connection connection = Mydb.getInstance().getConnection();

    public void ajouter(Battle b) {
        try {
            String sql = "INSERT INTO battle(scoreJoueur1, scoreJoueur2, gagnant) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, b.getScoreJoueur1());
            ps.setInt(2, b.getScoreJoueur2());
            ps.setString(3, b.getGagnant());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Battle b) {}
    @Override
    public void supprimer(Battle b) {}
    @Override
    public java.util.List<Battle> getAll() { return new java.util.ArrayList<>(); }
    @Override
    public Battle getById(int id) { return null; }
}