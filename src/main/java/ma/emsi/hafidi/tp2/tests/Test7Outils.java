package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;

public class Test7Outils {
    private static final String COORDONNEES = "Je vais à la latitude 34.02, longitude -6.84, pendant trois jours "
            + "à partir d'aujourd'hui. Est-ce que je prends un parapluie ?";
    private static final String REPORT = "Finalement, je pars demain. Je prends un parapluie ?";
    private static final String VILLE = "Je vais aujourd'hui à Tafraout, MA, pendant trois jours. "
            + "Dois-je prendre un parapluie ? Utilise les outils pour vérifier.";
    private static final String HORS_SUJET = "Explique en une phrase ce qu'est un volcan.";

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--logs")) {
            afficher(assistant(new MeteoTool(), true), VILLE);
            return;
        }
        Gemini.afficher(COORDONNEES, Gemini.modele(0.2, false).chat(COORDONNEES));
        var assistantCoordonnees = assistant(new PrevisionsSansGeocodage(), false);
        afficher(assistantCoordonnees, COORDONNEES);
        afficher(assistantCoordonnees, REPORT);
        afficher(assistantCoordonnees, HORS_SUJET);
        afficher(assistantCoordonnees, voyage("Rome"));
        afficher(assistantCoordonnees, voyage("Zzyzx"));
        var assistantVille = assistant(new MeteoTool(), false);
        afficher(assistantVille, VILLE);
        afficher(assistantVille, REPORT);
        afficher(assistantVille, HORS_SUJET);
        afficher(assistantVille, voyage("Villeimaginairexyz"));
    }

    private static String voyage(String ville) {
        return "Je vais aujourd'hui à " + ville + " pour trois jours. Dois-je prendre un parapluie ?";
    }

    private static AssistantMeteo assistant(Object outils, boolean traces) {
        return AiServices.builder(AssistantMeteo.class).chatModel(Gemini.modele(0.2, traces))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20)).tools(outils).build();
    }

    private static void afficher(AssistantMeteo assistant, String question) {
        Gemini.afficher(question, assistant.chat(question));
    }

    public static final class PrevisionsSansGeocodage {
        private final MeteoTool outil = new MeteoTool();

        @Tool("Indique si de la pluie est prévue pour des coordonnées géographiques et une période en jours.")
        public boolean isPluiePrevue(double latitude, double longitude, int nbJoursDebut, int nbJours) {
            return outil.isPluiePrevue(latitude, longitude, nbJoursDebut, nbJours);
        }
    }
}
