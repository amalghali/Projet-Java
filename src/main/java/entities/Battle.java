package entities;

public class Battle {
        private int id;
        private int scoreJoueur1;
        private int scoreJoueur2;
        private String gagnant;

        public Battle(int id, int scoreJoueur1, int scoreJoueur2, String gagnant) {
            this.id = id;
            this.scoreJoueur1 = scoreJoueur1;
            this.scoreJoueur2 = scoreJoueur2;
            this.gagnant = gagnant;
        }
        public int getId() {
            return id;
        }
        public int getScoreJoueur1() {
            return scoreJoueur1;
        }
        public int getScoreJoueur2() {
            return scoreJoueur2;
        }
        public String getGagnant() {
            return gagnant;
        }
        public void setId(int id) {
            this.id = id;
        }
        public void setScoreJoueur1(int scoreJoueur1) {
            this.scoreJoueur1 = scoreJoueur1;
        }

        public void setScoreJoueur2(int scoreJoueur2) {
            this.scoreJoueur2 = scoreJoueur2;
        }

        public void setGagnant(String gagnant) {
            this.gagnant = gagnant;
        }
    }

