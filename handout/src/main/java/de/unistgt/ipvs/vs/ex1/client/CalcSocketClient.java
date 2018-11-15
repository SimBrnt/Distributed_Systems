package de.unistgt.ipvs.vs.ex1.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.io.*;
import java.util.Scanner;
import java.io.PrintWriter;
import de.unistgt.ipvs.vs.ex1.server.CalculationImpl;
/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class
 * as necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcSocketClient {
	private Socket cliSocket;
	private int    rcvdOKs;		// --> Number of valid message contents
	private int    rcvdErs;		// --> Number of invalid message contents
	private int    calcRes;		// --> Calculation result (cf.  'RES')
	private BufferedReader in;

	
	public CalcSocketClient() {
		this.cliSocket = null;
		this.rcvdOKs   = 0;
		this.rcvdErs   = 0;
		this.calcRes   = 0;
	}
	public static void main(String[] args) {
		CalcSocketClient calcClient = new CalcSocketClient();
		String req31 = "  MUL  1   ASM  ADD ABC 10    5  SUB 100 ADD10   ADD";
		calcClient.connectTo("localhost", 2009);
		calcClient.calculate("24 foo 42 <" + (req31.length() + 5) + ":" + req31 + ">");
		calcClient.disconnect();
	}
	public int getRcvdOKs() {
		return rcvdOKs;
	}

	public int getRcvdErs() {
		return rcvdErs;
	}

	public int getCalcRes() {
		return calcRes;
	}

	public boolean connectTo(String srvIP, int srvPort) {
		
		try {
			this.cliSocket = new Socket(srvIP,srvPort);
			in = new BufferedReader (new InputStreamReader (this.cliSocket.getInputStream()));
			System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }		
		return true;
	}

	public boolean disconnect() {
               
		//Solution here
		try{
			this.cliSocket.close();               
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.cliSocket.isClosed());
		return true;
	}

	public boolean calculate(String request) {
            
		if (cliSocket == null) {
			System.err.println("Client not connected!");
			return false;
		}

		// FAIRE UN REGEX valid message
		// Trouver un moyen d'extraire avec un caractere d'un string
		int i;
		if(!request.contains("<")){
			this.rcvdErs++;
		}
		String[] reqB = request.split("<");
		String[] reqE = reqB[1].split(">");
		System.out.println("Taille tableau "+reqB.length);
		System.out.println("Case tableau "+ reqE[0]);
		//if(reqArray[0].startsWith("<")){
		//	String[] operation = reqArray[1].substring(0, (reqArray[1].length()-1)).split(" ");
		//	System.out.println("OPERATION "+operation);
		//Solution here
		try{
			CalculationImpl calc = new CalculationImpl();
			/*switch(operation[0])
              {
			  case "ADD":
				System.out.println("ON Rentre dans le ADD");
                for(i=1; i<operation.length; i++){
					calc.add(Integer.parseInt(operation[i].toString()));
				}
				System.out.println("CALC est "+ calc.getResult());
				//calcRes = calc.getResult();
                break;
              case 2:
            	  operation = dIn.readUTF();
            	  System.out.println("operation: " + operation);
                break;
              case 3:
            	  integer2 = dIn.readInt();
            	  done = true;
            	  System.out.println("integer2: " + integer2);
                break;*/
			 //}
			
			/** Parse la request pour savoir si elle est valide, ensuite faire un switch case pour connaitre l'operation, appeler la bonne opération suivant la requête 
			 * Réussir à gérer avec les in et out les requetes que le client envoie 
			 * Renvoyer le resultat
			 * pouri allant de 1 a operation.length
			*/
			//calc.add(10);
		}catch (RemoteException e){ 
			e.printStackTrace();
		}
	//}
		System.out.println("RETOURNE TRUE");
		return true;
	}
}
