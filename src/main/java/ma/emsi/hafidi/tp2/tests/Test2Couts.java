package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test2Couts {
    public static void main(String[] args) {
        var demande = ChatRequest.builder().messages(UserMessage.from(Gemini.QUESTION)).build();
        var reponse = Gemini.modele(0.7, false).chat(demande);
        Gemini.afficher(Gemini.QUESTION, reponse.aiMessage().text());
        var tokens = reponse.tokenUsage();
        int entree = tokens.inputTokenCount();
        int sortie = tokens.outputTokenCount();
        var cout = new BigDecimal("0.30").multiply(BigDecimal.valueOf(entree))
                .add(new BigDecimal("2.50").multiply(BigDecimal.valueOf(sortie))).movePointLeft(6);
        System.out.printf("Entrée : %d tokens ; sortie : %d tokens%n", entree, sortie);
        System.out.println("Coût estimé en dollars : " + cout.toPlainString());
        if (cout.signum() > 0) {
            System.out.println("Nombre de requêtes dans un budget de 1 $ : "
                    + BigDecimal.ONE.divide(cout, 0, RoundingMode.DOWN));
        }
    }
}
