package services;

import entities.Joueur;
import tools.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ServiceJoueur implements IService<Joueur> {

    private Connection connection = Mydb.getInstance().getConnection();

    public void ajouter(Joueur j) {
        try {
            String sql = "INSERT INTO joueur(points) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, j.getPoints());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Joueur j) {}
    @Override
    public void supprimer(Joueur j) {}
    @Override
    public java.util.List<Joueur> getAll() { return new java.util.ArrayList<>(); }
    @Override
    public Joueur getById(int id) { return null; }
}