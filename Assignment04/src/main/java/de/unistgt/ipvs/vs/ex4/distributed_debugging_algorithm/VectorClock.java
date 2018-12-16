package de.unistgt.ipvs.vs.ex4.distributed_debugging_algorithm;

//you are not allowed to change this class structure
public class VectorClock {

	protected int[] vectorClock;
	private int processId;
	private int numberOfProcesses;

	public VectorClock(int processId, int numberOfProcesses) {
		vectorClock = new int[numberOfProcesses];
		this.numberOfProcesses = numberOfProcesses;
		this.processId = processId;
	}

	VectorClock(VectorClock other) {
		vectorClock = other.vectorClock.clone();
		processId = other.processId;
		numberOfProcesses = other.numberOfProcesses;

	}

	public void increment() {
		// TODO
		/*
		 * Complete a code to increment the local clock component
		 */
		this.vectorClock[this.processId] += 1;
	}

	public int[] get() {
		// Complete a code to return the vectorClock value
		return this.vectorClock;
	}

	public void update(VectorClock other) {
		/*
		 * Implement Supremum operation
		 */
		int[] otherClock = other.get();
		for(int i = 0; i < this.numberOfProcesses; ++i)
			if(this.vectorClock[i] < otherClock[i])
				this.vectorClock[i] = otherClock[i];
	}

	public boolean checkConsistency(int otherProcessId, VectorClock other) {
		//TODO
		/*
		 * Implement a code to check if a state is consistent regarding two vector clocks (i.e. this and other).
		 * See slide 41 from global state lecture.
		 */
		int[] otherClock = other.get();
		return this.vectorClock[this.processId]>= otherClock[this.processId] && this.vectorClock[otherProcessId]<= otherClock[otherProcessId];
		//return this.vectorClock[otherProcessId] <= otherClock[otherProcessId];

	}
	
	@Override
	public String toString() {
		return "VC(" + vectorClock[0] + ", " + vectorClock[1] + ")";
	}

}
