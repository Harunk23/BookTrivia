<b>Book Trivia Quiz Applicatie</b> <br>
De Book Trivia Quiz Applicatie is een interactieve quiz waarin je jouw kennis over boeken en literatuur kunt testen. <br>
Bij het starten van de applicatie krijg je een reeks van 10 meerkeuzevragen, telkens met vier mogelijke antwoordopties. <br>
Wanneer je een vraag correct beantwoordt, verschijnt onderaan een melding “Correct!” en wordt je score automatisch met één verhoogd. <br>
Bij een fout antwoord wordt je gekozen optie donkerder weergegeven en toont de applicatie meteen wat het juiste antwoord was. <br>
Op deze manier krijg je direct feedback en kun je je score tijdens de quiz live volgen.

<b>Versies:</b> <br>
Spring boot versie: 4.0.2 <br>
Java: 21
JDK: ms21

<b>Bouw & Run de applicatie:</b> <br>
Laad maven (pom.xml - bestand) en het project zal bouwen.<br>

<b>Backend:</b> Navigeer in project folder BookTrivia -> src -> main -> java -> com.example.booktrivia -> BookTriviaApplication.

<b>Environment variabelen:</b> Klik rechtsboven op de applicatie -> edit configurations -> Modify options -> Environment variables. <br>
Voeg hierin het volgende: TRIVIA_API_URL="https://opentdb.com/api.php?amount=10&category=10&difficulty=easy" <br>
Klik daarna op apply en ok.

Run de applicatie om het backend op te starten. Deze is dan gestart op http://localhost:8081/.

Voor de unit testen navigeer in BookTrivia folder naar het test folder. Hierin is een subfolder genaamd Trivia.
Door deze te openen kan je de unit testen runnen.

<b>Frontend:</b> Navigeer vanuit de terminal naar de BookTriviaFrontend map d.m.v. "cd .\BookTriviaFrontend\" uit te voeren. <br> 
Voer een 'npm i' uit en daarna een 'ng serve'.<br>
Het frontend is nu gestart op http://localhost:4200/.
    
