package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import java.time.Duration;

final class Gemini {
    static final String MODELE = "gemini-3.5-flash-lite";
    static final String QUESTION = "Quelle est la capitale du Maroc ? Réponds en une phrase.";

    private Gemini() {
    }

    static String cle() {
        String cle = System.getenv("GEMINI_KEY");
        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException("Définissez la variable d'environnement GEMINI_KEY.");
        }
        return cle;
    }

    static ChatModel modele(double temperature, boolean traces) {
        String cle = cle();
        var builder = GoogleGenAiChatModel.builder()
                .modelName(MODELE).temperature(temperature)
                .timeout(Duration.ofSeconds(60)).maxRetries(2)
                .logRequests(traces).logResponses(traces);
        return (traces ? builder.client(TraceGemini.client(cle)) : builder.apiKey(cle)).build();
    }

    static void afficher(String question, String reponse) {
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse);
    }
}
