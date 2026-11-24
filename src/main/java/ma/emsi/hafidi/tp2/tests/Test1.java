package ma.emsi.hafidi.tp2.tests;

public class Test1 {
    public static void main(String[] args) {
        var modele = Gemini.modele(0.7, false);
        for (String question : new String[]{Gemini.QUESTION, "Quelle heure est-il à Rabat en ce moment ?",
                "Bonjour, je m'appelle Rayane Hafidi. Réponds brièvement.", "Quel est mon nom ?"}) {
            Gemini.afficher(question, modele.chat(question));
        }
    }
}
