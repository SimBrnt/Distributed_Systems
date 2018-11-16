package de.unistgt.ipvs.vs.ex1.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.unistgt.ipvs.vs.ex1.common.Command;
import de.unistgt.ipvs.vs.ex1.common.ICalculation;
import de.unistgt.ipvs.vs.ex1.common.MessageUtils;

/**
 * Add fields and methods to this class as necessary to fulfill the assignment.
 */
public class CalculationSession extends Thread {
	Socket cliSocket;
	
	public CalculationSession(Socket socket) {
		this.cliSocket = socket;
	}
	
	@Override
	public void run() {
		try {
			ObjectOutputStream oosOut = new ObjectOutputStream(this.cliSocket.getOutputStream());
			ObjectInputStream oisIn = new ObjectInputStream(this.cliSocket.getInputStream());
			
			String request = (String) oisIn.readObject();
			System.out.println("RECEIVED: " + request);
			
			Command[] cmds = MessageUtils.parse(request);
			
			for(Command cmd : cmds) {
				
			}
			//System.out.println("COMPUTED ANSWER: " + computeRequest(request));
			
			oosOut.close();
			oisIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}