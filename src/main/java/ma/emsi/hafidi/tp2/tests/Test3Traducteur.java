package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.model.input.PromptTemplate;
import java.util.Map;

public class Test3Traducteur {
    public static void main(String[] args) {
        var modele = Gemini.modele(0.2, false);
        var fixe = PromptTemplate.from("Traduis en anglais, sans commentaire : {{texte}}");
        var variable = PromptTemplate.from("Traduis en {{langue}}, sans commentaire : {{texte}}");
        for (var prompt : java.util.List.of(fixe.apply(Map.of("texte", "Le train arrive à Rabat.")),
                variable.apply(Map.of("langue", "espagnol", "texte", "Le train arrive à Rabat.")))) {
            Gemini.afficher(prompt.text(), modele.chat(prompt.text()));
        }
    }
}
