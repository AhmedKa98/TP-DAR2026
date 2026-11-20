# TP 4 — Chat Client/Serveur avec sockets UDP (mode non-connecté)

**Matière :** Développement d'applications réparties
**Auditoire :** LSI 3 — Faculté des Sciences de Sfax — 2026/2027
**Enseignante des TPs :** Mme Zeineb DAOUD
**Étudiant :** Ahmed KARRAY
**IDE utilisé :** IntelliJ IDEA

---

## Sommaire

- `etape1/` — Chat unidirectionnel : un client envoie, le serveur affiche
- `etape2/` — Chat bidirectionnel : le serveur diffuse chaque message aux autres clients


## Principe d'UDP (mode non-connecté)

Avec UDP, il n'y a ni connexion préalable ni flux (pas de `accept()` comme en TCP).
Chaque message est un datagramme indépendant qui contient les données, l'adresse et le port de destination.
L'envoi se fait avec `send()`, la réception avec `receive()`, sans garantie de livraison ni d'ordre.

---

## Étape 1 — Chat unidirectionnel

### Serveur (`ServeurUDP`)

- `new DatagramSocket(null)` crée une socket non liée, puis `bind(new InetSocketAddress(1234))` la lie au port 1234.
- Dans une boucle infinie, `receive(paquet)` bloque jusqu'à l'arrivée d'un datagramme.
- Seuls les `getLength()` octets reçus sont convertis en `String`.
- Le serveur affiche l'IP et le port de l'expéditeur (`getAddress()`, `getPort()`) et le contenu.

### Client (`ClientUDP`)

- `new DatagramSocket()` : le système choisit un port local libre.
- Le message est construit au format `[NomUtilisateur] : texte`, converti en octets, placé dans un `DatagramPacket` avec l'adresse et le port du serveur, puis envoyé par `send()`.

### Captures d'écran

<!-- CAPTURE 1 : console du serveur ServeurUDP recevant le message (IP, port, contenu) -->
![Étape 1 — Serveur](images/tp4_1_serveur.png)

<!-- CAPTURE 2 : console du client ClientUDP après l'envoi du message -->
![Étape 1 — Client](images/tp4_2_client.png)

---

## Étape 2 — Chat bidirectionnel avec diffusion

### Serveur (`ServeurChatUDP`)

Il garde un `Set<SocketAddress>` des clients connus. Un ensemble évite les doublons.
À chaque message reçu :

- l'adresse IP:port de l'expéditeur est ajoutée si elle est nouvelle ;
- le message est renvoyé à tous les clients enregistrés sauf l'expéditeur ;
- le message `/quit` retire le client de la liste et prévient les autres.

### Client (`ClientChatUDP`)

`receive()` est bloquant : il faut donc deux threads pour envoyer et recevoir en même temps.

- **Thread de réception** : boucle sur `receive()` et affiche les messages entrants.
- **Thread principal** : lit le clavier et envoie les messages.

Au démarrage, le client envoie « a rejoint le chat ». Ce premier datagramme permet au serveur d'enregistrer son adresse, sans quoi il ne recevrait aucun message tant qu'il n'a rien écrit.

### Captures d'écran

<!-- CAPTURE 3 : console du serveur ServeurChatUDP (clients enregistrés, messages) -->
![Étape 2 — Serveur](images/tp4_3_serveur_chat.png)

<!-- CAPTURE 4 : console du client 1 -->
![Étape 2 — Client 1](images/tp4_4_client1.png)

<!-- CAPTURE 5 : console du client 2 (reçoit les messages du client 1, puis test de /quit) -->
![Étape 2 — Client 2](images/tp4_5_client2.png)

---

## Synthèse

- UDP est non connecté : le serveur identifie un client uniquement grâce à l'adresse IP:port source du datagramme reçu.
- `receive()` est bloquant : côté client, un thread dédié à la réception est indispensable pour un chat en temps réel.
- Taille du buffer et `getLength()` : on ne lit que les octets réellement reçus, sinon on récupère des données résiduelles.
- L'encodage UTF-8 est imposé des deux côtés.
- Limites d'UDP : pas de garantie de livraison, d'ordre ni de détection de perte. Un client qui plante sans `/quit` reste dans la liste du serveur.
- Améliorations possibles : pseudo unique, messages privés, détection des clients inactifs, interface graphique.
