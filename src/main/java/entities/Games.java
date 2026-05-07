package entities;

public class Games {
    private int id;
    private String typeJeux;
    private String difficulte;
    private int timeLimit;
    private int scoreMax;

    public Games() {
    }

    public Games(int id, String typeJeux, String difficulte, int timeLimit, int scoreMax) {
        this.id = id;
        this.typeJeux = typeJeux;
        this.difficulte = difficulte;
        this.timeLimit = timeLimit;
        this.scoreMax = scoreMax;
    }

    public Games(String typeJeux, String difficulte, int timeLimit, int scoreMax) {
        this.typeJeux = typeJeux;
        this.difficulte = difficulte;
        this.timeLimit = timeLimit;
        this.scoreMax = scoreMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeJeux() {
        return typeJeux;
    }

    public void setTypeJeux(String typeJeux) {
        this.typeJeux = typeJeux;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(int scoreMax) {
        this.scoreMax = scoreMax;
    }

    @Override
    public String toString() {
        return "Games{" +
                "id=" + id +
                ", typeJeux='" + typeJeux + '\'' +
                ", difficulte='" + difficulte + '\'' +
                ", timeLimit=" + timeLimit +
                ", scoreMax=" + scoreMax +
                '}';
    }
}
