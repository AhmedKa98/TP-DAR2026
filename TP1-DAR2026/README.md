# TP1-DAR2026 --- Les sockets en mode connecté TCP

**Enseignante des TPs :** Mme Zeineb DAOUD\
**Étudiant :** Ahmed Karray

## Sommaire

-   [Activité 1-1 : Première connexion Client/Serveur](#activité-1-1--première-connexion-clientserveur)
-   [Activité 1-2 : Service de calcul](#activité-1-2--service-de-calcul)
-   [Activité 1-3 : Amélioration du service](#activité-1-3--amélioration-du-service)

------------------------------------------------------------------------

## Activité 1-1 : Première connexion Client/Serveur

Cette première activité permet de mettre en place une communication TCP
entre un client et un serveur.

### Serveur --- `Server.java`

``` java
package serverPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1_1 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("je suis un serveur en attente la connexion d'un client ");
            Socket clientSocket = serverSocket.accept();
            System.out.println("un client est connecte");
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
```

### Client --- `Client.java`

``` java
package clientPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1_1 {

    public static void main(String[] args) {
        try {
            System.out.println("je suis un client pas encore connecte..");
            Socket socket = new Socket("127.0.0.1", 1234);
            System.out.println("client connecte au serveur");
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
```

### Résultat attendu

-   Le serveur attend une connexion sur le port `1234`.
-   Le client se connecte au serveur et lui envoie un message.
-   Le serveur affiche le message reçu et renvoie une confirmation.

------------------------------------------------------------------------

## Activité 1-2 : Service de calcul

Dans cette activité, le serveur reçoit un entier envoyé par le client,
calcule son produit par 5 et renvoie le résultat. La communication
utilise `DataInputStream` et `DataOutputStream`.

### Serveur --- `ServerCalcul.java`

``` java
package serverPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1_2_1 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket =new ServerSocket(1234);
            System.out.println("je suis un serveur en attente la connexion d'un client ");
            Socket clientSocket = serverSocket.accept();
            System.out.println("un client est connecté");
            DataInputStream  in  = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            while (true) {
                int x = in.readInt();
                if (x == 0) {                      
                    System.out.println("le client a envoyé 0 : fin de la communication.");
                    break;
                }
                int resultat = x * 5;
                System.out.println("Reçu :" + x + " ->  envoye : " + resultat);
                out.writeInt(resultat);
                out.flush();
            }
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("sockets fermés côté serveur.");

        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
```

### Client --- `ClientCalcul.java`

``` java
package clientPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1_2_1 {

    public static void main(String[] args) {
        try {
            System.out.println("je suis un client pas encore connecte..");
            Socket socket =new Socket("127.0.0.1", 1234);
            System.out.println("client connecté au serveur");
            DataOutputStream out =new DataOutputStream(socket.getOutputStream());
            DataInputStream  in  =new DataInputStream(socket.getInputStream());
            Scanner sc =new Scanner(System.in);
            int x;
            do {
                System.out.print("entrez un entier (0 pour quitter) : ");
                x= sc.nextInt();
                out.writeInt(x);                  
                out.flush();
                if (x!= 0) {
                    int resultat = in.readInt();
                    System.out.println("Résultat (x * 5) = " + resultat);
                }
            } while (x!= 0);
            System.out.println("fin de la communication.");
            sc.close();
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
```

### Résultat attendu

Le client peut envoyer plusieurs entiers au serveur. Pour chaque entier
reçu, le serveur calcule `nombre * 5` et retourne le résultat.

------------------------------------------------------------------------

## Activité 1-3 : Amélioration du service

Cette dernière activité transforme le service précédent en une
calculatrice TCP. Le client choisit une opération parmi l'addition, la
soustraction, la multiplication et la division, puis envoie les deux
nombres au serveur.

### Serveur --- `ServerCalculatrice.java`

``` java
package serverPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1_3 {
    private static final int PORT = 1234;
    public static void main(String[] args) {
        // IP reelle du serveur : passee en argument,sinon IP détectee automatiquement
        String ipServeur = (args.length > 0) ? args[0] : null;

        try {
            InetAddress adresse = (ipServeur != null)
                    ? InetAddress.getByName(ipServeur)
                    : InetAddress.getLocalHost();
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(adresse, PORT));
            System.out.println("serveur en ecoute sur " + adresse.getHostAddress() + ":" + PORT);
            System.out.println("se suis un serveur en attente la connexion d'un client ");
            Socket clientSocket = serverSocket.accept();
            System.out.println("un client est connecte : "
                    + clientSocket.getInetAddress().getHostAddress());
            DataInputStream  in  = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            while (true) {
                int choix = in.readInt();
                if (choix == 0) {
                    System.out.println("Le client a quitté.");
                    break;
                }
                int x = in.readInt();
                int y = in.readInt();
                int statut = 0;
                double resultat = 0;
                switch (choix) {
                    case 1: resultat = x + y;
                    break;
                    case 2: resultat = x - y; 
                    break;
                    case 3: resultat =(double) x * y; 
                    break;
                    case 4:
                        if (y == 0) statut =1;               // division par 0
                        else resultat =(double) x / y;
                        break;
                    default: statut =2;                       // operation inconnue
                }

                System.out.println("Operation " + choix + ":" + x + "," + y
                        + "  ->  statut=" + statut + " resultat=" + resultat);
                out.writeInt(statut);
                if (statut == 0) {
                    out.writeDouble(resultat);
                }
                out.flush();
            }
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Sockets fermés côté serveur.");
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
```

### Client --- `ClientCalculatrice.java`

``` java
package clientPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client1_3 {
    private static final int PORT = 1234;
    public static void main(String[] args) {
        // IP de la machine serveur : en argument, sinon saisie au clavier
        Scanner sc = new Scanner(System.in);
        String ipServeur;
        if (args.length > 0) {
            ipServeur = args[0];
        } else {
            System.out.print("Adresse IP du serveur : ");
            ipServeur = sc.nextLine().trim();
        }
        try {
            System.out.println("Je suis un client pas encore connecté…");
            Socket socket =new Socket();
            socket.connect(new InetSocketAddress(ipServeur, PORT), 5000); // 5s
            System.out.println("Client connecté au serveur " + ipServeur + ":" + PORT);
            DataOutputStream out =new DataOutputStream(socket.getOutputStream());
            DataInputStream  in  =new DataInputStream(socket.getInputStream());
            int choix;
            do {
                System.out.println("\n/*-+ CALCULATRICE +-*/");
                System.out.println("1 - Addition");
                System.out.println("2 - Soustraction");
                System.out.println("3 - Multiplication");
                System.out.println("4 - Division");
                System.out.println("0 - Quitter");
                System.out.print("votre choix : ");
                choix = sc.nextInt();
                if (choix == 0) {
                    out.writeInt(0);              
                    out.flush();
                    break;
                }
                if (choix < 1 || choix > 4) {
                    System.out.println("choix invalide, recommencez.");
                    continue;
                }
                System.out.print("Entrez x : ");
                int x = sc.nextInt();
                System.out.print("Entrez y : ");
                int y = sc.nextInt();
                out.writeInt(choix);
                out.writeInt(x);
                out.writeInt(y);
                out.flush();
                int statut = in.readInt();
                if (statut == 0) {
                    double resultat = in.readDouble();
                    System.out.println("Résultat = " + resultat);
                } else if (statut == 1) {
                    System.out.println("Erreur : division par 0 impossible.");
                } else {
                    System.out.println("Erreur : operation inconnue.");
                }
            } while (true);

            System.out.println("Fin de la communication.");
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        sc.close();
    }
}
```

### Résultat attendu

Le client sélectionne une opération et saisit deux nombres. Le serveur
effectue le calcul demandé et renvoie le résultat. Le cas de la division
par zéro et les choix invalides sont également traités.

------------------------------------------------------------------------

## Conclusion

Ce TP m'a permis de comprendre le fonctionnement des sockets TCP et les
échanges de données entre un client et un serveur en Java. Les trois
activités montrent progressivement comment établir une connexion,
envoyer des données et construire un service de calcul à distance.
