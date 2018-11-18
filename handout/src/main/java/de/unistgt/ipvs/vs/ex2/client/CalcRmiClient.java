package de.unistgt.ipvs.vs.ex2.client;

import de.unistgt.ipvs.vs.ex2.common.ICalculation;
import de.unistgt.ipvs.vs.ex2.common.ICalculationFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Implement the getCalcRes-, init-, and calculate-method of this class as
 * necessary to complete the assignment. You may also add some fields or
 * methods.
 */
public class CalcRmiClient {
	private ICalculation calc = null;

	public CalcRmiClient() {
		this.calc = null;
	}

	public int getCalcRes() {
		try {
			return calc.getResult();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean init(String url) {
		try {
			calc = (ICalculation) Naming.lookup(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean calculate(CalculationMode calcMode, Collection<Integer> numbers) {
		try {
			switch (calcMode) {
			case ADD:
				for (int n : numbers) calc.add(n);
				break;
			case MUL:
				for (int n : numbers) calc.multiply(n);
				break;
			case SUB:
				for (int n : numbers) calc.subtract(n);
				break;
			}
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
}
