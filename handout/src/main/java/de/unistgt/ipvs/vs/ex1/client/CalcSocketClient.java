package de.unistgt.ipvs.vs.ex1.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class
 * as necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcSocketClient {
	private Socket cliSocket;
	private int    rcvdOKs;		// --> Number of valid message contents
	private int    rcvdErs;		// --> Number of invalid message contents
	private int    calcRes;		// --> Calculation result (cf.  'RES')
	
	public CalcSocketClient() {
		this.cliSocket = null;
		this.rcvdOKs   = 0;
		this.rcvdErs   = 0;
		this.calcRes   = 0;
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
			cliSocket = new Socket(srvIP, srvPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean disconnect() {
               
	    //Solution here
               
		return true;
	}

	public boolean calculate(String request) {
               
		if (cliSocket == null) {
			System.err.println("Client not connected!");
			return false;
		}
		
		try {
			ObjectOutputStream oosOut = new ObjectOutputStream(cliSocket.getOutputStream());
			ObjectInputStream oisIn = new ObjectInputStream(cliSocket.getInputStream());
			
			System.out.println("SENDING: " + request);
			oosOut.writeObject(request);
			
			oosOut.close();
			oisIn.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
