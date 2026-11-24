package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.service.SystemMessage;

public interface AssistantMeteo {
    @SystemMessage("Réponds en français et brièvement. Utilise les outils pour vérifier les prévisions de pluie. "
            + "Si un outil ne trouve pas la ville ou ne fournit pas les données, dis-le. "
            + "Pour une autre question, réponds normalement.")
    String chat(String question);
}
