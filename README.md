# TP 2 — Tests LangChain4j de Hafidi

Projet Maven Java 25. Définir `GEMINI_KEY` dans l'environnement Windows et relancer IntelliJ si nécessaire. Les programmes se lancent à la racine du projet, depuis IntelliJ ou en ligne de commande.

```powershell
mvn compile exec:java "-Dexec.mainClass=ma.emsi.hafidi.tp2.tests.Test1"
```

Classes disponibles : `Test1`, `Test2Couts`, `Test3Traducteur`, `Test4Embeddings`, `Test5Rag1`, `Test6Rag2`, `TestMeteo` et `Test7Outils`.

Le test 6 lit les questions en console. `Test7Outils` accepte l'argument `--logs` pour lancer une session neuve avec les deux outils et afficher les échanges. Les journaux HTTP restent locaux.

[Énoncé du TP 2](http://richard.grin.free.fr/emsi/rabat-ia/tp/tp2-langchain4j/index.html).
