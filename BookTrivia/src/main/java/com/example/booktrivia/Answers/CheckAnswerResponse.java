package com.example.booktrivia.Answers;

public class CheckAnswerResponse {
    // Hierin wordt de boolean meegegeven of het geslaag is of niet en wat het juiste antwoord is
    private boolean correct;
    private String correctAnswer;

    public CheckAnswerResponse() {
    }

    public CheckAnswerResponse(boolean correct) {
        this.correct = correct;
    }

    public CheckAnswerResponse(boolean correct, String correctAnswer) {
        this.correct = correct;
        this.correctAnswer = correctAnswer;
    }

    public boolean getIsCorrect() {
        return correct;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
