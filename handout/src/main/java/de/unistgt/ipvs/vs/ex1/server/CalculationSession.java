package de.unistgt.ipvs.vs.ex1.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.unistgt.ipvs.vs.ex1.common.ICalculation;

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
			System.out.println("COMPUTED ANSWER: " + computeRequest(request));
			
			oosOut.close();
			oisIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String computeRequest(String request) {
		String payload;
		try {
			payload = request.split("[<>]")[1];
		} catch(ArrayIndexOutOfBoundsException e) {
			return "ERR";
		}
		
		String[] parts = payload.split(":");
		int dataLen;
		try {
			if(parts[0].length() == 2)
				dataLen = Integer.valueOf(parts[0]);
			else return "ERR";
		} catch (NumberFormatException e) {
			return "ERR";
		}
		
		// Accounting for <, >, XX and :
		dataLen -= 5;
		
		String curCmd = null;
		for(String particle : parts[1].split(" ")) {
			int particleLen = particle.length();
			dataLen -= particleLen + 1;
			if(particle.length() == 0) continue;
			switch(particle) {
			case "ADD":
			case "SUB":
			case "MUL":
			case "RES":
				curCmd = particle;
				break;
			default:
				System.out.println(curCmd + " " + particle);
			}
		}
		
		if (dataLen == -1) return "OK";
		else return "ERR";
	}
}