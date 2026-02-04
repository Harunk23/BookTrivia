package com.example.booktrivia.Trivia;

import com.example.booktrivia.Answers.CheckAnswerRequest;
import com.example.booktrivia.Answers.CheckAnswerResponse;
import com.example.booktrivia.OpenTdb.OpenTdbQuestion;
import com.example.booktrivia.OpenTdb.OpenTdbResponse;
import com.example.booktrivia.Answers.AnswerStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class TriviaService {
    // De log wordt gebruikt om foutmeldingen te loggen
    private static final Logger log = LoggerFactory.getLogger(TriviaService.class);

    // Het juiste antwoord komt in dit object
    private final AnswerStore answerStore;

    // JSON wordt geparsed met het object mapper
    private final ObjectMapper objectMapper;

    // API call wordt gemaakt met het client
    private final HttpClient httpClient;

    // Het URL van openTdb met custom url parameters
    private final String triviaApiUrl = System.getenv("TRIVIA_API_URL");

    public TriviaService(AnswerStore answerStore, ObjectMapper objectMapper) {
        this.answerStore = answerStore;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Haalt alle trivia-vragen op, verwerkt deze en retourneert ze zonder
     * het correcte antwoord expliciet mee te geven.
     * @return een lijst met objecten zonder correcte antwoorden.
     */
    public List<TriviaQuestion> fetchQuestions() {
        // Als de openTdb url leeg is of onbekend retourneren we een leeg lijst.
        if (triviaApiUrl == null || triviaApiUrl.isBlank()) {
            return List.of();
        }

        // Response wordt opgehaald
        OpenTdbResponse apiResponse = fetchApiResponse(triviaApiUrl);

        // We retourneren een leeg lijst als het response faalt
        if (apiResponse == null || apiResponse.getResults() == null) {
            return List.of();
        }

        // We willen de vragen opslaan zonder het correcte antwoord
        List<TriviaQuestion> questions = new ArrayList<>();

        for (OpenTdbQuestion incoming : apiResponse.getResults()) {
            // We geven hier een uniek Id mee (makkelijker op dit moment om het zo te doen zonder een DB)
            String id = String.valueOf((int) (Math.random() * 1000000));

            // Het lijst houdt alle antwoorden bij
            List<String> answers = new ArrayList<>();

            if (incoming.getIncorrect_answers() != null) {
                answers.addAll(incoming.getIncorrect_answers());
            }
            if (incoming.getCorrect_answer() != null) {
                answers.add(incoming.getCorrect_answer());
            }

            // Hier slaan we de correcte antwoord op
            if (incoming.getCorrect_answer() != null) {
                answerStore.save(id, incoming.getCorrect_answer());
            }

            // Hier geven we alle data mee aan het TriviaQuestion object
            questions.add(new TriviaQuestion(
                    id,
                    incoming.getCategory(),
                    incoming.getDifficulty(),
                    incoming.getQuestion(),
                    answers
            ));
        }

        return questions;
    }

    /**
     * Controleert of het door de gebruiker gekozen antwoord correct is.
     * @param answer het object dat het ID van de vraag en het door de gebruiker gekozen antwoord bevat.
     * @return een response die aangeeft of het antwoord correct is.
     */
    public CheckAnswerResponse checkAnswer(CheckAnswerRequest answer) {
        if (answer == null) {
            return new CheckAnswerResponse(false);
        }
        String correctAnswer = answerStore.getAndRemove(answer.getQuestionId());
        boolean correct = correctAnswer != null && correctAnswer.equals(answer.getChosenAnswer());
        return new CheckAnswerResponse(correct, correctAnswer);
    }

    /**
     * Het api response van openTdb wordt opgehaald.
     * @param triviaApiUrl het url van openTdb.
     * @return het response van het opgehaalde openTdb API.
     */
    public OpenTdbResponse fetchApiResponse(String triviaApiUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(triviaApiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.debug("Response status is not 200");
                return emptyResponse();
            }
            return objectMapper.readValue(response.body(), OpenTdbResponse.class);
        } catch (Exception e) {
            log.debug("The error is: {}", e.getMessage());
            return emptyResponse();
        }
    }

    /**
     * We retourneren een leeg respoonse.
     * Dit doen we omdat we niet bij een fout de applicatie een exceptie willen laten gooien.
     * @return het lege response die we meegeven.
     */
    private OpenTdbResponse emptyResponse() {
        OpenTdbResponse response = new OpenTdbResponse();
        response.setResults(List.of());
        return response;
    }
}
