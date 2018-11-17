package de.unistgt.ipvs.vs.ex1.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
			
			while(true) {
				try {					
					String request = (String) oisIn.readObject();
					oosOut.writeObject(MessageUtils.generate("OK"));
					
					try {
						String[] cmds = MessageUtils.split(request);
						
						for(String cmd : cmds) {
							switch(cmd) {
							case "ADD":
							case "SUB":
							case "MUL":
							case "RES":
								break;
							default:
								if(cmd.matches("-?\\d+"))
									break;
								else {
									oosOut.writeObject(MessageUtils.generate("ERR " + cmd));
									continue;
								}
							}
							oosOut.writeObject(MessageUtils.generate("OK"));
						}
					} catch(RuntimeException e) {
						// In case of blocking error send BRKERR
						oosOut.writeObject(MessageUtils.generate("BRKERR"));
					}
					
					oosOut.writeObject(MessageUtils.generate("FIN"));
				} catch (EOFException e) {
					break;
				}				
			}
			
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