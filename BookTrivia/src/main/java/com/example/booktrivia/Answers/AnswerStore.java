package com.example.booktrivia.Answers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerStore {
    // Houdt de juiste antwoord bij d.m.v. het vraag id te linken met het juiste antwoord.
    private final Map<String, String> answers = new HashMap<>();

    /**
     * Slaat het juiste antwoord op met het bijbehorende vraag id.
     * @param questionId de id van de vraag.
     * @param correctAnswer de juiste antwoord die bij het betreffende vraag hoort.
     */
    public void save(String questionId, String correctAnswer) {
        if (questionId == null || correctAnswer == null) {
            return;
        }
        answers.put(questionId, correctAnswer);
    }

    /**
     * Haalt het juiste antwoord op en verwijdert het direct uit de map.
     * Een vraag wordt een keer gesteld, beantwoord en gecontroleerd.
     * Daarna is het correcte antwoord niet meer nodig en wordt het
     * verwijderd om onnodig geheugenverbruik te voorkomen.
     * @param questionId het id van de vraag.
     * @return het juiste antwoord.
     */
    public String getAndRemove(String questionId) {
        if (questionId == null) {
            return null;
        }
        return answers.remove(questionId);
    }
}
