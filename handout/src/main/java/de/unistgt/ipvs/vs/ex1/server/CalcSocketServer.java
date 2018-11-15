package de.unistgt.ipvs.vs.ex1.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
			srvSocket = new ServerSocket(port);
			while(true) {
				new CalcClientServerThread(srvSocket.accept()).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

class CalcClientServerThread extends Thread {
	Socket cliSocket;
		
	public CalcClientServerThread(Socket socket) {
		this.cliSocket = socket;
	}
	
	@Override
	public void run() {
		try {
			ObjectOutputStream oosOut = new ObjectOutputStream(this.cliSocket.getOutputStream());
			ObjectInputStream oisIn = new ObjectInputStream(this.cliSocket.getInputStream());
			
			String request = (String) oisIn.readObject();
			System.out.println("RECEIVED: " + request);
			
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