package com.example.booktrivia.Answers;

public class CheckAnswerRequest {
    // Variabelen, het vraagId en gekozen antwoord worden in dit klasse opgeslagen
    private String questionId;
    private String chosenAnswer;

    public CheckAnswerRequest() { }

    public CheckAnswerRequest(String questionId, String chosenAnswer) {
        this.questionId = questionId;
        this.chosenAnswer = chosenAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getChosenAnswer() {
        return chosenAnswer;
    }
}