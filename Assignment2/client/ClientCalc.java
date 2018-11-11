import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ClientCalc {
    public static void main(String[] args) {

        String machine = "localhost";
        int port=1099;
        Socket socket2;
        BufferedReader in;
        PrintWriter out;
        PrintWriter msg;
        Scanner sc;
        Scanner recommencer;
        boolean boucle=true;
        String rec;

        try {
            Registry registry = LocateRegistry.getRegistry(machine, port);
            
            socket2 = new Socket(InetAddress.getLocalHost(),2009);
            System.out.println("Demande de connexion");
            in = new BufferedReader (new InputStreamReader (socket2.getInputStream()));
            String message_distant = in.readLine();
            while(boucle) {
            System.out.println(message_distant);

            sc = new Scanner(System.in);

            System.out.print("Enter Integer 1: ");
            int integer1 = sc.nextInt();

            System.out.print("Enter operation (+, -, *, /): ");
            String operation = sc.next();
            System.out.print("Enter Integer 2: ");
            int integer2 = sc.nextInt();
            System.out.println("Requested: " + integer1 + operation + integer2);

            DataOutputStream dOut = new DataOutputStream(socket2.getOutputStream());
         // Send first message
            dOut.writeByte(1);
            dOut.writeInt(integer1);
            dOut.flush();
         // Send the second message
            dOut.writeByte(2);
            dOut.writeUTF(operation);
            dOut.flush();

            // Send the third message
            dOut.writeByte(3);
            dOut.writeInt(integer2);
            dOut.flush(); // Send off the data

            // Send the exit message
            dOut.writeByte(-1);
            dOut.flush();

           // dOut.close();

            message_distant = in.readLine();
            System.out.println(message_distant);

            recommencer = new Scanner(System.in);
            System.out.println("Voulez vous refaire un calcul? yes/no");
            rec = recommencer.nextLine();
              if (rec.equals("no")){
                boucle=false;
                msg = new PrintWriter ("no");
                msg.println ("no");
                socket2.close();
              }
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
        	e.printStackTrace();
        }



  }
}
