package de.unistgt.ipvs.vs.ex1.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import de.unistgt.ipvs.vs.ex1.common.MessageUtils;

/**
 * Implement the connectTo-, disconnect-, and calculate-method of this class
 * as necessary to complete the assignment. You may also add some fields or methods.
 */
public class CalcSocketClient {
	private Socket cliSocket;
	private int    rcvdOKs;		// --> Number of valid message contents
	private int    rcvdErs;		// --> Number of invalid message contents
	private int    calcRes;		// --> Calculation result (cf.  'RES')
	
	private ObjectOutputStream oosOut;
	private ObjectInputStream oisIn;
	
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
			oosOut = new ObjectOutputStream(cliSocket.getOutputStream());
			oisIn = new ObjectInputStream(cliSocket.getInputStream());
			
			String rdyMsg = (String) oisIn.readObject();
			if(!MessageUtils.split(rdyMsg)[0].equals("RDY")) {
				System.err.println("The server did not send the RDY message.");
				return false;
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean disconnect() {
               
		try {
			oosOut.close();
			oisIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
               
		return true;
	}

	public boolean calculate(String request) {
        
		if (cliSocket == null) {
			System.err.println("Client not connected!");
			return false;
		}
		
		try {
			
			oosOut.writeObject(request);
			
			boolean finReceived = false;
			while(!finReceived) {
				String ans = (String) oisIn.readObject();
				boolean mustReadResult = false;
				for(String cmd : MessageUtils.split(ans)) {
					if(mustReadResult) {
						try {
							this.calcRes = Integer.parseInt(cmd);
						} catch (NumberFormatException e) {
							System.err.println("The server replied with an invalid message.");
						}
						mustReadResult = false;
					} else switch(cmd) {
						case "FIN":
							finReceived = true;
							break;
						case "OK":
							rcvdOKs += 1;
							break;
						case "ERR":
							rcvdErs += 1;
							break;
						case "RES":
							mustReadResult = true;
							break;
						case "BRKERR":
							System.err.println("The request generated a server error and could not be handled.");
							break;
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
