package com.example.booktrivia.Trivia;

import java.util.List;

public class TriviaQuestion {
    // Hier slaan we de vraag op zonder het juiste antwoord
    private String id;
    private String category;
    private String difficulty;
    private String question;
    private List<String> answers;

    public TriviaQuestion() {
    }

    public TriviaQuestion(String id, String category, String difficulty, String question, List<String> answers) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
