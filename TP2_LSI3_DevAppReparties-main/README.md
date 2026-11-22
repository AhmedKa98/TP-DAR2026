# TP2 : Sockets TCP en mode connecté

**Étudiant :** Karray Ahmed\
**Groupe :** LSI 3\
**Matière :** Développement d'applications réparties\
**Enseignante des TPs :** Mme Zeineb DAOUD\
**Année universitaire :** 2026--2027


## Structure du projet

``` text
src/
├── activite2_1/                
│   ├── Serveur.java
│   ├── Client.java               
│
└── activite2_2/                 
    ├── Operation.java
    ├── Serveur.java
    └── Client.java                
```

## Activité 2-1 : Échange de chaînes de caractères

Le client envoie au serveur le premier opérande, l'opérateur et le
deuxième opérande, chacun sur une ligne. Le serveur effectue le calcul
et renvoie le résultat.

La syntaxe de l'expression est vérifiée côté client à l'aide d'une
expression régulière avant l'envoi des données.

**Classes utilisées :** - `BufferedReader` - `InputStreamReader` -
`PrintWriter`

## Activité 2-2 : Échange d'objets

Le client envoie un objet `Operation`, qui implémente l'interface
`Serializable` et contient les deux opérandes ainsi que l'opérateur. Le
serveur reçoit cet objet, effectue le calcul et renvoie le résultat au
client.

**Classes utilisées :** - `Serializable` - `ObjectOutputStream` -
`ObjectInputStream`

## Interprétation des résultats

### Activité 2-1 : Échange de chaînes de caractères

-   Le client envoie l'opération en trois lignes : opérande 1, opérateur
    et opérande 2.
-   Le serveur effectue le calcul et renvoie le résultat. Par exemple,
    `55 * 25` donne `1375.0`.
-   Le résultat est décimal (`1375.0`) parce que les opérandes sont
    convertis en `double`.
-   Une division par zéro (`10 / 0`) renvoie un message d'erreur sans
    arrêter le serveur.
-   Les expressions invalides (`25 ** 55`, `abc + 3`) sont rejetées par
    le client grâce à l'expression régulière, avant tout envoi.
-   La saisie de `FIN` permet de fermer proprement les deux programmes.

### Activité 2-2 : Échange d'objets

-   Le client envoie un seul objet `Operation` (`Serializable`) au lieu
    de transmettre trois chaînes séparées.
-   Le serveur reçoit l'objet, lit ses attributs, effectue le calcul et
    renvoie le même résultat (`1375.0`). Cela confirme le bon
    fonctionnement de la sérialisation.
-   Les types des données sont conservés (`double`, `char`) : aucune
    conversion n'est nécessaire côté serveur.
-   Les flux doivent être créés dans cet ordre : `ObjectOutputStream`,
    puis `flush()`, puis `ObjectInputStream`. Le respect de cet ordre
    évite le blocage des deux programmes.

### Comparaison des deux approches

Les deux méthodes permettent d'obtenir le même résultat.

-   **Échange de chaînes :** simple à mettre en place, mais nécessite de
    définir et de gérer manuellement un protocole de communication.
-   **Échange d'objets :** plus structuré et plus facile à faire
    évoluer, mais nécessite que la classe `Operation` soit disponible
    des deux côtés et reste spécifique à Java.

## Conclusion

Ce TP m'a permis de comprendre comment échanger des chaînes de
caractères et des objets entre un client et un serveur à l'aide des
sockets TCP en Java. Il m'a également permis de comparer deux méthodes
de communication : l'envoi de données textuelles et la sérialisation
d'objets.
