package de.unistgt.ipvs.vs.ex1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import de.unistgt.ipvs.vs.ex1.server.CalculationSession;

/**
 * Extend the run-method of this class as necessary to complete the assignment.
 * You may also add some fields, methods, or further classes.
 */
public class CalcSocketServer extends Thread {
	private ServerSocket srvSocket;
	private int port;

	public CalcSocketServer(int port) {
		this.srvSocket = null;
		
		this.port = port;
	}
	
	public static void main(String[] args){
		CalcSocketServer calc = new CalcSocketServer(2009);
		calc.run();
	}
	@Override
	public void interrupt() {
		try {
			if (srvSocket != null) srvSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
           
		if (port <= 0) {
			System.err.println("Wrong number of arguments.\nUsage: SocketServer <listenPort>\n");
			System.exit(-1);
		}

		// Start listening server socket ..
		try {
			this.srvSocket = new ServerSocket(this.port);
			Thread t = new Thread(new CalculationSession(this.srvSocket));
			t.start();
			System.out.println("Serveur prêt pour servir plusieurs clients !");
			} catch (IOException e) {
	
				e.printStackTrace();
			}
	}
        
        public void waitUnitlRunnig(){
            while(this.srvSocket == null){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
            }
        }
}