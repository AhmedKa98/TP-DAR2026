# TP 3 — Serveur Multi-threads (sockets TCP)

**Matière :** Développement d'applications réparties
**Auditoire :** LSI 3 — Faculté des Sciences de Sfax — 2026/2027
**Enseignante des TPs :** Mme Zeineb DAOUD
**Étudiant :** Ahmed KARRAY
**IDE utilisé :** IntelliJ IDEA

---

## Sommaire

- [Objectif](#objectif)
- [Environnement](#environnement)
- [Exécution dans IntelliJ IDEA](#exécution-dans-intellij-idea)
- [Activité 3-1 : acceptation de plusieurs clients](#activité-3-1--acceptation-de-plusieurs-clients)
- [Activité 3-2 : calculatrice multi-thread](#activité-3-2--calculatrice-multi-thread)
- [Conclusion](#conclusion)

---

## Objectif

Dans les TP1 et TP2, le serveur ne pouvait traiter qu'un seul client par exécution.
Ce TP transforme le squelette Client/Serveur en un véritable serveur multi-thread capable de :

- accepter plusieurs clients simultanément (un thread dédié par client) ;
- identifier chaque client (adresse IP + numéro d'ordre de connexion) ;
- exposer un service de calculatrice par échange d'objets sérialisés ;
- protéger une ressource critique (compteur global d'opérations) contre les accès concurrents.


## Activité 3-1 : acceptation de plusieurs clients

### 3.1.1 Principe

Le thread principal ne fait qu'une chose : boucler sur `accept()`. À chaque connexion il :

- incrémente le numéro d'ordre du client ;
- affiche son adresse IP via `socket.getRemoteSocketAddress()` ;
- lance un `ClientProcess` puis retourne immédiatement à `accept()`.

### Résultat des tests locaux

<!-- CAPTURE 1 : console IntelliJ du serveur avec au moins 2 clients connectés -->
![Serveur — plusieurs clients connectés](images/tp3_1_serveur.png)

<!-- CAPTURE 2 : consoles des clients (onglets Run) -->
![Clients connectés](images/tp3_2_clients.png)

---

## Activité 3-2 : calculatrice multi-thread

### 3.2.1 Principe

Le service de calculatrice du TP2 est intégré au serveur multi-thread.
Client et serveur échangent des objets `Operation` qui transportent à la fois la requête (opérandes, opérateur) et la réponse (résultat, erreur, compteur global).

### Résultat des tests locaux

<!-- CAPTURE 3 : console serveur + console client montrant des opérations et le compteur global -->
![Calculatrice multi-thread](images/tp3_3_calculatrice.png)

---

## Conclusion

Ce TP a permis de passer d'un serveur séquentiel à un serveur concurrent basé sur le modèle « un thread par client », d'échanger des objets sur TCP et de protéger une ressource partagée par synchronisation.
