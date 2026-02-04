package com.example.booktrivia.Trivia;

import com.example.booktrivia.Answers.CheckAnswerRequest;
import com.example.booktrivia.Answers.CheckAnswerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TriviaController {
    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    // We halen de vragen op zonder het juiste antwoord te tonen
    @GetMapping("/questions")
    public List<TriviaQuestion> getQuestions() {
        return triviaService.fetchQuestions();
    }

    // We controleren het gegeven antwoord
    @PostMapping("/checkanswers")
    public CheckAnswerResponse checkAnswers(@RequestBody CheckAnswerRequest request) {
        return triviaService.checkAnswer(request);
    }
}