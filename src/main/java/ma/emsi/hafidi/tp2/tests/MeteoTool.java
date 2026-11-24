package ma.emsi.hafidi.tp2.tests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

public class MeteoTool {
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final double seuilPluie;

    public MeteoTool() {
        this(25.0);
    }

    public MeteoTool(double seuilPluie) {
        this.seuilPluie = seuilPluie;
    }

    @Tool("Indique si la probabilité de pluie dépasse le seuil pendant la période demandée (sur les 7 prochains jours).")
    public boolean isPluiePrevue(@P("Latitude") double latitude, @P("Longitude") double longitude,
                                 @P("Début en jours après aujourd'hui ; 0 signifie aujourd'hui") int nbJoursDebut,
                                 @P("Nombre de jours de séjour") int nbJours) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude) || Math.abs(latitude) > 90
                || Math.abs(longitude) > 180 || nbJoursDebut < 0 || nbJoursDebut > 6
                || nbJours < 1 || nbJours > 7 - nbJoursDebut) {
            throw new IllegalArgumentException("Coordonnées ou période hors des 7 jours de prévision.");
        }
        int fin = nbJoursDebut + nbJours;
        String uri = String.format(Locale.ROOT,
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&daily=precipitation_probability_max&timezone=auto&forecast_days=%d",
                latitude, longitude, fin);
        var probabilites = lire(uri).getAsJsonObject("daily").getAsJsonArray("precipitation_probability_max");
        for (int i = nbJoursDebut; i < fin; i++) {
            if (probabilites.get(i).isJsonNull()) {
                throw new IllegalStateException("Probabilité de pluie indisponible.");
            }
            if (probabilites.get(i).getAsDouble() > seuilPluie) {
                return true;
            }
        }
        return false;
    }

    @Tool("Trouve la latitude et la longitude d'une ville. Retourne [latitude, longitude] pour alimenter l'outil météo.")
    public double[] getCoordonnees(@P("Nom de la ville, facultativement suivi d'une virgule et du code pays ISO à deux lettres") String ville) {
        String[] lieu = ville.split(",", 2);
        String uri = "https://geocoding-api.open-meteo.com/v1/search?count=1&language=en&name="
                + URLEncoder.encode(lieu[0].strip(), StandardCharsets.UTF_8);
        if (lieu.length == 2) {
            uri += "&countryCode=" + URLEncoder.encode(lieu[1].strip(), StandardCharsets.UTF_8);
        }
        var resultats = lire(uri).getAsJsonArray("results");
        if (resultats == null || resultats.isEmpty()) {
            throw new IllegalArgumentException("Aucune ville trouvée pour " + ville);
        }
        var premier = resultats.get(0).getAsJsonObject();
        return new double[]{premier.get("latitude").getAsDouble(), premier.get("longitude").getAsDouble()};
    }

    private JsonObject lire(String url) {
        var demande = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(15)).GET().build();
        try {
            var reponse = client.send(demande, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (reponse.statusCode() != 200) {
                throw new IllegalStateException("Open-Meteo HTTP " + reponse.statusCode());
            }
            return JsonParser.parseString(reponse.body()).getAsJsonObject();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Appel Open-Meteo interrompu.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de joindre Open-Meteo.", e);
        }
    }
}
