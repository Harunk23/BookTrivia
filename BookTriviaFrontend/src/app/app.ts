import { Component, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

/**
 * Model voor een trivia-vraag zoals die van de backend komt
 */
type TriviaQuestion = {
  id: string;
  category: string;
  difficulty: string;
  question: string;
  answers: string[];
};

/**
 * Model voor de response van het controleren van een antwoord
 */
type CheckAnswerResponse = {
  correct: boolean;
  correctAnswer?: string;
};

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  // HttpClient wordt geïnjecteerd om API-calls te kunnen doen
  private readonly http = inject(HttpClient);

  // Basis-URL van de backend API
  private readonly apiBase = 'http://localhost:8081';

  // Geeft aan of de applicatie bezig is met laden
  protected readonly loading = signal(true);

  // Bevat een foutmelding als er iets misgaat
  protected readonly error = signal<string | null>(null);

  // Lijst met alle trivia-vragen
  protected readonly questions = signal<TriviaQuestion[]>([]);

  // Index van de huidige vraag
  protected readonly currentIndex = signal(0);

  // Het antwoord dat de gebruiker heeft geselecteerd
  protected readonly selectedAnswer = signal<string | null>(null);

  // Resultaat van het gegeven antwoord (true = correct, false = fout)
  protected readonly answerResult = signal<boolean | null>(null);

  // Het correcte antwoord (alleen gevuld na het beantwoorden)
  protected readonly correctAnswer = signal<string | null>(null);

  // Geeft aan of het antwoord momenteel gecontroleerd wordt
  protected readonly isAnswering = signal(false);

  // Aantal correct beantwoorde vragen
  protected readonly correctCount = signal(0);

  // Totaal aantal vragen (afgeleid van de questions-lijst)
  protected readonly totalQuestions = computed(() => this.questions().length);

  // De huidige vraag op basis van de huidige index
  protected readonly currentQuestion = computed(
    () => this.questions()[this.currentIndex()] ?? null
  );

  // Geeft aan of de quiz is afgelopen
  protected readonly isFinished = computed(
    () => this.totalQuestions() > 0 && this.currentIndex() >= this.totalQuestions()
  );

  // Constructor wordt één keer aangeroepen bij het starten van de app
  constructor() {
    // Laad de vragen zodra de applicatie start
    this.loadQuestions();
  }

  /**
   * Haalt de trivia-vragen op bij de backend
   */
  private loadQuestions(): void {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<TriviaQuestion[]>(`${this.apiBase}/questions`).subscribe({
      next: (questions) => {
        // Decodeert HTML-entities in vragen en antwoorden
        const decoded = (questions ?? []).map((question) => ({
          ...question,
          question: this.decodeHtml(question.question),
          answers: question.answers?.map((answer) => this.decodeHtml(answer)) ?? []
        }));

        // Slaat de vragen op in de state
        this.questions.set(decoded);

        // Reset alle quiz-statussen
        this.currentIndex.set(0);
        this.selectedAnswer.set(null);
        this.answerResult.set(null);
        this.correctAnswer.set(null);
        this.correctCount.set(0);

        this.loading.set(false);
      },
      error: () => {
        // Wordt aangeroepen als de vragen niet opgehaald kunnen worden
        this.error.set('Could not load questions. Please try again later.');
        this.loading.set(false);
      }
    });
  }

  /**
   * Wordt aangeroepen wanneer de gebruiker een antwoord kiest
   */
  protected selectAnswer(answer: string): void {
    const question = this.currentQuestion();

    // Stop als er geen vraag is of als er al een antwoord is gegeven
    if (!question || this.answerResult() !== null || this.isAnswering()) {
      return;
    }

    this.selectedAnswer.set(answer);
    this.isAnswering.set(true);

    // Stuurt het gekozen antwoord naar de backend
    this.http
      .post<CheckAnswerResponse>(`${this.apiBase}/checkanswers`, {
        questionId: question.id,
        chosenAnswer: answer
      })
      .subscribe({
        next: (response) => {
          // Zet het resultaat van het antwoord
          this.answerResult.set(response?.correct);
          this.correctAnswer.set(response?.correctAnswer ?? null);

          // Verhoog de score als het antwoord correct is
          if (this.answerResult()) {
            this.correctCount.update((count) => count + 1);
          }

          this.isAnswering.set(false);
        },
        error: () => {
          // Wordt aangeroepen als het antwoord niet gecontroleerd kan worden
          this.error.set('Could not check the answer. Please try again.');
          this.isAnswering.set(false);
        }
      });
  }

  /**
   * Gaat naar de volgende vraag
   */
  protected nextQuestion(): void {
    this.currentIndex.update((index) => index + 1);
    this.selectedAnswer.set(null);
    this.answerResult.set(null);
    this.correctAnswer.set(null);
  }

  /**
   * Start de quiz opnieuw
   */
  protected restartQuiz(): void {
    this.loadQuestions();
  }

  /**
   * Zet HTML-entities (zoals &quot; of &#039;) om naar leesbare tekst
   */
  private decodeHtml(value: string): string {
    if (!value) {
      return '';
    }
    const textarea = document.createElement('textarea');
    textarea.innerHTML = value;
    return textarea.value;
  }
}
