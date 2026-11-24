package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Test6Rag2 {
    public static void main(String[] args) {
        var assistant = Test5Rag1.assistant(FileSystemDocumentLoader.loadDocument("ml.pdf"));
        if (args.length > 0) {
            for (String question : args) {
                Gemini.afficher(question, assistant.chat(question));
            }
            return;
        }
        try (var clavier = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println("Posez une question sur le PDF. Une ligne vide termine la session.");
            while (clavier.hasNextLine()) {
                var question = clavier.nextLine();
                if (question.isBlank()) {
                    break;
                }
                Gemini.afficher(question, assistant.chat(question));
            }
        }
    }
}
