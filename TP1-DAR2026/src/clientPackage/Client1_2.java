package clientPackage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1_2 {

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
