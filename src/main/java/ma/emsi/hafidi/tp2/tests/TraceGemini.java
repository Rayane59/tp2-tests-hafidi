package ma.emsi.hafidi.tp2.tests;

import com.google.genai.Client;
import com.google.genai.types.ClientOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okio.Buffer;

import java.util.concurrent.TimeUnit;

final class TraceGemini {
    private TraceGemini() {
    }

    static Client client(String cle) {
        var http = new OkHttpClient.Builder().callTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    var demande = chain.request();
                    if (demande.body() != null) {
                        var corps = new Buffer();
                        demande.body().writeTo(corps);
                        afficher("Application → Gemini (requête)", corps.readUtf8(), cle);
                    }
                    var reponse = chain.proceed(demande);
                    afficher("Gemini → Application (réponse HTTP " + reponse.code() + ")",
                            reponse.peekBody(1024 * 1024).string(), cle);
                    return reponse;
                })
                .build();
        return Client.builder().apiKey(cle)
                .clientOptions(ClientOptions.builder().customHttpClient(http).build()).build();
    }

    private static void afficher(String sens, String json, String cle) {
        String sansCle = json.replace(cle, "[clé masquée]");
        try {
            sansCle = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
                    .toJson(JsonParser.parseString(sansCle));
        } catch (com.google.gson.JsonParseException e) {
            // Une erreur réseau peut renvoyer autre chose qu'un objet JSON.
        }
        System.out.println("=== " + sens + " ===");
        System.out.println(sansCle);
    }
}
