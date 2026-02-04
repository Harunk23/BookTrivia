package com.example.booktrivia.Trivia;

import com.example.booktrivia.Answers.AnswerStore;
import com.example.booktrivia.Answers.CheckAnswerRequest;
import com.example.booktrivia.Answers.CheckAnswerResponse;
import com.example.booktrivia.OpenTdb.OpenTdbQuestion;
import com.example.booktrivia.OpenTdb.OpenTdbResponse;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TriviaServiceTest {

    @Mock
    private AnswerStore answerStore;

    @Mock
    private ObjectMapper objectMapper;

    private TriviaService service;

    @BeforeEach
    void setUp() {
        service = spy(new TriviaService(answerStore, objectMapper));
    }

    /**
     * Test of we een leeg lijst doorsturen bij een lege API URL.
     */
    @Test
    void fetchQuestionsEmptyUrlTest() {
        List<TriviaQuestion> result = service.fetchQuestions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(answerStore);
    }

    /**
     * Test dat we false retourneren bij een onbeantwoorde vraag.
     */
    @Test
    void checkAnswerNullTest() {
        CheckAnswerResponse response = service.checkAnswer(null);

        assertNotNull(response);
        assertFalse(response.getIsCorrect());
        verifyNoInteractions(answerStore);
    }

    /**
     * Test dat we het juiste antwoord selecteren.
     */
    @Test
    void checkAnswerCorrectAnswerTest() {
        when(answerStore.getAndRemove("q1")).thenReturn("A");

        CheckAnswerRequest req = new CheckAnswerRequest("q1", "A");
        CheckAnswerResponse response = service.checkAnswer(req);

        assertEquals("A", response.getCorrectAnswer());
    }

    /**
     * Test dat we het onjuiste antwoord selecteren.
     */
    @Test
    void checkAnswerWrongAnswerTest() {
        when(answerStore.getAndRemove("q1")).thenReturn("A");

        CheckAnswerRequest req = new CheckAnswerRequest("q1", "B");
        CheckAnswerResponse response = service.checkAnswer(req);

        assertFalse(response.getIsCorrect());
        assertEquals("A", response.getCorrectAnswer());

        verify(answerStore).getAndRemove("q1");
    }
}