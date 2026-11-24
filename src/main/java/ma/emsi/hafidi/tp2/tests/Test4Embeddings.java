package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel.TaskTypeEnum;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Test4Embeddings {
    public static void main(String[] args) {
        String[][] paires = {
                {"Le train arrive à la gare.", "Le train entre en gare."},
                {"La porte est ouverte.", "La porte est fermée."},
                {"La porte est ouverte.", "Un dauphin nage dans l'océan."},
                {"J'ai emprunté un roman.", "Je suis allé à la bibliothèque."},
                {"The meeting begins at noon.", "The meeting starts at twelve."},
                {"La pluie tombe sur Rabat.", "It is raining in Rabat."}
        };
        EmbeddingModel local = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingModel google = GoogleGenAiEmbeddingModel.builder().apiKey(Gemini.cle())
                .modelName("gemini-embedding-001").taskType(TaskTypeEnum.SEMANTIC_SIMILARITY)
                .timeout(Duration.ofSeconds(30)).build();
        List<dev.langchain4j.data.segment.TextSegment> textes = Arrays.stream(paires)
                .flatMap(Arrays::stream).map(dev.langchain4j.data.segment.TextSegment::from).toList();
        var vecteursLocaux = local.embedAll(textes).content();
        var vecteursGoogle = google.embedAll(textes).content();
        System.out.println("Phrase A | Phrase B | score local | score Google");
        for (int i = 0; i < paires.length; i++) {
            System.out.printf(Locale.ROOT, "%s | %s | %.4f | %.4f%n", paires[i][0], paires[i][1],
                    CosineSimilarity.between(vecteursLocaux.get(2 * i), vecteursLocaux.get(2 * i + 1)),
                    CosineSimilarity.between(vecteursGoogle.get(2 * i), vecteursGoogle.get(2 * i + 1)));
        }
    }
}
