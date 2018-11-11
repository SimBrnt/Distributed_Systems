/*
Fonction main (
récupération du numéro de port
création de la socket serveur
création
d’une socket
création du Buffer et du Printer
boucle(
récupération des donnees du Client
gestion des exception, des mauvaises saisies,  si pas de problèmes
[calcul]
sinon
[création message d’erreur]
si message d’erreur existe
[envoie du messag
e au client]
sinon
[envoie du résultat au client]
)
fermeture des objets, des sockets */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.EOFException;



public class ServeurCalc {

    public static void main(String[] args) {
    	ServerSocket socketserver  ;
        Socket socket1 ;
        BufferedReader in;
        PrintWriter out;
        boolean boucle=true;


        try {
            socketserver = new ServerSocket(2009);
            System.out.println("[Server] Le serveur est à l'écoute du port "+socketserver.getLocalPort());
            socket1 = socketserver.accept();
            System.out.println("Un client s'est connecté");
            out = new PrintWriter(socket1.getOutputStream());
            out.println("Vous êtes connecté !");
            out.flush();
            while (boucle) {

            System.out.println("debut calcul");

            int integer1 = -1;
            int integer2 = -1;
            String operation = "";
            DataInputStream dIn = new DataInputStream(socket1.getInputStream());
            boolean done = false;
            while(!done) {

              byte messageType = dIn.readByte();

              switch(messageType)
              {
              case 1:
                integer1 = dIn.readInt();
                System.out.println("integer1: " + integer1);
                break;
              case 2:
            	  operation = dIn.readUTF();
            	  System.out.println("operation: " + operation);
                break;
              case 3:
            	  integer2 = dIn.readInt();
            	  done = true;
            	  System.out.println("integer2: " + integer2);
                break;
             }
            }

            System.out.println("[Server] Requested from client: " + integer1 + operation + integer2);

            int res = -1;
            switch(operation){
	              case "+":
	            	  res = integer1 + integer2;
	            	  break;

	              case "-":
	            	  res = integer1 - integer2;
	            	  break;

	              case "*":
	            	  res = integer1 * integer2;
	            	  break;

	              case "/":
	            	  if(integer2 == 0){
	            		  res = -1;
	            		  break;
	            	  }
	            	  res = integer1 / integer2;
	            	  break;
            }
            in = new BufferedReader (new InputStreamReader (socket1.getInputStream()));

            if(in.toString().equals("no")){
              socket1.close();
              socketserver.close();
              boucle=false;
        }

            System.out.println("[Server] res =" + res);

            out.println("Res: " + res);
            out.flush();



      }
        }catch (IOException e) {
              e.printStackTrace();
        }


}
}
