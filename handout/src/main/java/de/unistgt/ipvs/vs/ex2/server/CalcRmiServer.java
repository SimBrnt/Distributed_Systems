package de.unistgt.ipvs.vs.ex2.server;

import de.unistgt.ipvs.vs.ex2.common.ICalculationFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implement the run-method of this class to complete
 * the assignment. You may also add some fields or methods.
 */
public class CalcRmiServer extends Thread {
	private String regHost;
	private String objName;
        String url; //Please use this variable to bind the object.
	
	public CalcRmiServer(String regHost, String objName) {
		this.regHost = regHost;
		this.objName = objName;
                this.url = "rmi://" + regHost + "/" + objName;
	}
	@Override
	public void run() {
		if (regHost == null || objName == null) {
			System.err.println("<registryHost> or <objectName> not set!");
			return;
		}
                
                //Add solution here
	}

        public void stopServer(){
            try {
                Naming.unbind(url);
            } catch (RemoteException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CalcRmiServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
}
