package com.example.booktrivia.OpenTdb;

import java.util.List;

public class OpenTdbQuestion {
    // Alle binnenkomende data die we met dit object vastleggen
    private String difficulty;
    private String category;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public OpenTdbQuestion() {

    }

    public OpenTdbQuestion(String difficulty, String category, String question, String correct_answer, List<String> incorrect_answers) {
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }
}
