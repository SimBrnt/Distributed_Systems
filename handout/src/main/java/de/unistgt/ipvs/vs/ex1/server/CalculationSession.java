package de.unistgt.ipvs.vs.ex1.server;

import de.unistgt.ipvs.vs.ex1.common.ICalculation;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Add fields and methods to this class as necessary to fulfill the assignment.
 */

public class CalculationSession implements Runnable {

    private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	private BufferedReader in;
	private PrintWriter out;
    public CalculationSession(ServerSocket s){
        socketserver = s;
    }
         public void run() {
            try {
                while(true){

                    socket = socketserver.accept(); // Un client se connecte on l'accepte
                    in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("<08:RDY>");
                    out.flush();
                    System.out.println("Le client numéro "+nbrclient+ " est connecté !");
                    nbrclient++;
                    socket.close();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
         }
}