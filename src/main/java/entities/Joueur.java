package entities;

public class Joueur {
    private int id;
    private int points;

    public Joueur(int id, int points) {
        this.id = id;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}
