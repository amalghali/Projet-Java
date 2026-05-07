package main;

import entities.*;
import services.*;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        ServiceGames sg = new ServiceGames();
        ServiceBattle sb = new ServiceBattle();
        ServiceJoueur sj = new ServiceJoueur();
        ServicePartie sp = new ServicePartie();

        Games game = new Games(0, "Quiz", "Facile", 60, 100);
        sg.ajouter(game);
        System.out.println("game ajouté ");

        Battle battle = new Battle(0, 80, 70, "Joueur1");
        sb.ajouter(battle);
        System.out.println("battle ajouté ");

        Joueur joueur = new Joueur(0, 200);
        sj.ajouter(joueur);
        System.out.println("joueur ajouté ");

        Joueur j = new Joueur(1, 200);
        Partie partie = new Partie(0, 90, Date.valueOf("2024-05-01"), j);
        sp.ajouter(partie);
        System.out.println("partie ajoutée ");

        System.out.println("on a ajouté tous");
    }
}