package ma.emsi.hafidi.tp2.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class Test5Rag1 {
    public interface Assistant {
        String chat(String question);
    }

    static Assistant assistant(Document document) {
        var store = new InMemoryEmbeddingStore<TextSegment>();
        EmbeddingStoreIngestor.ingest(document, store);
        return AiServices.builder(Assistant.class).chatModel(Gemini.modele(0.2, false))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(store)).build();
    }

    public static void main(String[] args) {
        String question = "Comment s'appelle le chat de Pierre ?";
        Gemini.afficher(question, Gemini.modele(0.2, false).chat(question));
        var document = FileSystemDocumentLoader.loadDocument("infos.txt");
        var rag = assistant(document);
        Gemini.afficher(question, rag.chat(question));
        var appel = "Pierre appelle son chat. Qu'est-ce qu'il pourrait dire ?";
        Gemini.afficher(appel, rag.chat(appel));

        var documentModifie = Document.from(document.text().replace(
                "La capitale de la France est Paris.", "La capitale de la France est Marseille."));
        var capitale = "Quelle est la capitale de la France ?";
        Gemini.afficher(capitale, assistant(documentModifie).chat(capitale));
    }
}
