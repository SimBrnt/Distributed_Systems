package de.unistgt.ipvs.vs.ex1.server;

import de.unistgt.ipvs.vs.ex1.common.ICalculation;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;



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
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("<08:RDY>");
                    out.flush();
                    System.out.println("Le client numéro "+nbrclient+ " est connecté !");
                    nbrclient++;
                    in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
                    String req = in.readLine();
                    System.out.println("REQ "+req);
                    String[] length = req.split(":");
                    System.out.println(length.length);
                    String[] finalReq = length[1].split(" ");
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

                    CalculationImpl calc;
                        //HERE MANIPULATE THE REQ TO COMPUTE WHAT WE WANT
                        //Doing it with if statements ? 
                    for(int i=0; i<finalReq.length; i++){
                        dOut.writeByte(i);
                        if(finalReq[i].matches("ADD")){
                            System.out.println("ADD :)");
                            dOut.writeUTF("<07:OK>");
                            dOut.flush();
                        }else if(finalReq[i].matches("SUB")){
                            System.out.println("SUB :)");
                            dOut.writeUTF("<07:OK>");
                            dOut.flush();
                        }else if(finalReq[i].matches("MUL")){
                            System.out.println("MUL :)");
                            dOut.writeUTF("<07:OK>");
                            dOut.flush();
                        }else if(finalReq[i].matches("RES")){
                            System.out.println("RES :)");
                            dOut.writeUTF("<07:OK>");
                            dOut.flush();
                        }else if(finalReq[i].matches("^-?\\d+$")){
                            System.out.println("Int :)");
                            dOut.writeUTF("<07:OK>");
                            dOut.flush();
                        }else if (finalReq[i] != "" && finalReq[i] != " "){
                            System.out.println("Not a if");
                            dOut.writeUTF("<"+":ERR "+">");
                            dOut.flush();
                        }
                        /*switch(finalReq[i])
                            {
                            case "ADD":
                                    System.out.println("ADD :)");
                                    out.println("<07:OK>");
                                    //Here ask to compute for the integer following but before another request
                                break;
                            case "SUB":
                                    System.out.println("SUB :)");
                                    out.println("<07:OK>");
                                break;
                            case "MUL":
                                    System.out.println("MUL :)");
                                    out.println("<07:OK>");
                                break;
                            case " ":
                                    System.out.println("Space");
                            break;
                            case "^-?\\d+$":
                                    System.out.println("INTEGER");
                                    out.println("<07:OK>");
                            break;
                            case "RES": 
                                    out.println("<07:OK> RES ");//+calc.getResult()
                            default: 
                                    System.out.println("PAS UN CASE");
                                    out.println("<08:ERR");
                            }*/
                        System.out.println(finalReq[i]);
                    }
                    socket.close();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
         }
}