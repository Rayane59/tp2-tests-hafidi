# Utilisation de l’IA — TP 2 Tests

## 1. Modèle d’embeddings local

Le test 4 ne compilait pas avec les imports initiaux. La version d’Easy RAG utilisée fournit le modèle BGE Small English v1.5 dans sa variante quantifiée. L’IA a corrigé les imports et utilisé ce modèle pour comparer les six paires de phrases avec le modèle de Google.

## 2. Géocodage d’une ville

Open-Meteo n’a renvoyé aucun résultat pour Akchour. L’IA a vérifié plusieurs noms de villes et retenu Tafraout. Gemini a ensuite pu obtenir ses coordonnées avec le premier outil et les transmettre à l’outil météo.

## 3. Récupération du JSON des appels d’outils

Les journaux standard de LangChain4j affichaient principalement des objets Java. L’IA a ajouté une trace qui affiche le JSON des requêtes et réponses HTTP en masquant la clé. Cette trace a permis d’isoler les fragments `functionCall` et `functionResponse` demandés pour le bonus.

Le test météo a aussi montré une limite : `false` signifie qu’aucune probabilité de pluie ne dépasse le seuil de 25 %. Gemini l’a présenté comme une absence de pluie prévue, ce qui est plus catégorique que le résultat de l’outil.
