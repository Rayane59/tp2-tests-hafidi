package ma.emsi.hafidi.tp2.tests;

public class TestMeteo {
    public static void main(String[] args) {
        for (double seuil : new double[]{10, 25, 75}) {
            boolean pluie = new MeteoTool(seuil).isPluiePrevue(34.02, -6.84, 0, 3);
            System.out.printf("Rabat, trois jours, seuil %.0f %% : %s%n", seuil, pluie);
        }
    }
}
