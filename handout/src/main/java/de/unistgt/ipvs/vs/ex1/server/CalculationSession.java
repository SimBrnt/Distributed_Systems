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
	
	enum Operation {
		Add, Subtract, Multiply
	}
	
	public CalculationSession(Socket socket) {
		this.cliSocket = socket;
	}
	
	@Override
	public void run() {
		try {
			ObjectOutputStream oosOut = new ObjectOutputStream(this.cliSocket.getOutputStream());
			ObjectInputStream oisIn = new ObjectInputStream(this.cliSocket.getInputStream());
			
			ICalculation calc = new CalculationImpl();
			Operation curOp = null;
			
			while(true) {
				try {					
					String request = (String) oisIn.readObject();
					oosOut.writeObject(MessageUtils.generate("OK"));
					
					try {
						String[] cmds = MessageUtils.split(request);
						
						for(String cmd : cmds) {
							switch(cmd) {
							case "ADD":
								curOp = Operation.Add;
								break;
							case "SUB":
								curOp = Operation.Subtract;
								break;
							case "MUL":
								curOp = Operation.Multiply;
								break;
							case "RES":
								oosOut.writeObject(MessageUtils.generate("RES " + calc.getResult()));
								break;
							default:
								if(cmd.matches("-?\\d+")) {
									int operand = Integer.parseInt(cmd);
									switch(curOp) {
									case Add:
										calc.add(operand);
										break;
									case Subtract:
										calc.subtract(operand);
										break;
									case Multiply:
										calc.multiply(operand);
										break;
									}
									break;
								} else {
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

