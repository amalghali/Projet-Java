package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class QuizController {

    @FXML private Label lblQuestion;
    @FXML private Label lblScore;
    @FXML private Label lblTimer;
    @FXML private ProgressBar progressTimer;
    @FXML private Button btnOpt1, btnOpt2, btnOpt3;

    private int score = 0;
    private int timeLeft = 10;
    private Timeline timeline;
    private int currentQuestionIndex = 0;

    private List<Question> questions = new ArrayList<>();

    private static class Question {
        String text;
        String[] options;
        String correct;
        Question(String t, String[] o, String c) { text = t; options = o; correct = c; }
    }

    @FXML
    public void initialize() {
        questions.add(new Question("Quel langage est utilisé pour le Web ?", new String[]{"Java", "HTML", "C++"}, "HTML"));
        questions.add(new Question("Que signifie 'JDK' ?", new String[]{"Java Dev Kit", "Java Data Key", "Join Dev Kit"}, "Java Dev Kit"));
        questions.add(new Question("Quel symbole termine une instruction en Java ?", new String[]{":", ";", "."}, ";"));
        questions.add(new Question("Lequel est un langage de script ?", new String[]{"Python", "Assembly", "C"}, "Python"));

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            lblQuestion.setText(q.text);
            btnOpt1.setText(q.options[0]);
            btnOpt2.setText(q.options[1]);
            btnOpt3.setText(q.options[2]);
            
            timeLeft = 10;
            startTimer();
        } else {
            lblQuestion.setText("Quiz Terminé ! Score final: " + score);
            btnOpt1.setDisable(true);
            btnOpt2.setDisable(true);
            btnOpt3.setDisable(true);
        }
    }

    private void startTimer() {
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft--;
            updateTimerUI();
            if (timeLeft <= 0) {
                timeline.stop();
                nextQuestion();
            }
        }));
        timeline.setCycleCount(10);
        timeline.play();
    }

    private void updateTimerUI() {
        lblTimer.setText("Temps: " + timeLeft + "s");
        progressTimer.setProgress(timeLeft / 10.0);
    }

    @FXML
    private void handleAnswer(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        if (clicked.getText().equals(questions.get(currentQuestionIndex).correct)) {
            score += 10;
        }
        lblScore.setText("Score: " + score);
        nextQuestion();
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }
}
