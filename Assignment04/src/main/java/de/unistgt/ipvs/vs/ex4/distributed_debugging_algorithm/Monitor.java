package de.unistgt.ipvs.vs.ex4.distributed_debugging_algorithm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//you are not allowed to change this class structure. However, you can add local functions!
public class Monitor implements Runnable {

	/**
	 * The state consists on vector timestamp and local variables of each
	 * process. In this class, a state is represented by messages (events)
	 * indices of each process. The message contains a local variable and vector
	 * timestamp, see Message class. E.g. if state.processesMessagesCurrentIndex
	 * contains {1, 2}, it means that the state contains the second message
	 * (event) from process1 and the third message (event) from process2
	 */
	private class State {
		// Message indices of each process
		private int[] processesMessagesCurrentIndex;

		public State(int numberOfProcesses) {
			processesMessagesCurrentIndex = new int[numberOfProcesses];
		}

		public State(int[] processesMessagesCurrentIndex) {
			this.processesMessagesCurrentIndex = processesMessagesCurrentIndex;
		}

		{
			processesMessagesCurrentIndex = new int[numberOfProcesses];
		}

		public int[] getProcessesMessagesCurrentIndex() {
			return processesMessagesCurrentIndex;
		}

		public int getProcessMessageCurrentIndex(int processId) {
			return this.processesMessagesCurrentIndex[processId];
		}

		@Override
		public boolean equals(Object other) {
			State otherState = (State) other;

			// Iterate over processesMessagesCurrentIndex array
			for (int i = 0; i < numberOfProcesses; i++)
				if (this.processesMessagesCurrentIndex[i] != otherState.processesMessagesCurrentIndex[i])
					return false;

			return true;
		}
		
		@Override
		public String toString() {
			String s = "State: ";
			for(int i = 0; i < numberOfProcesses; ++i) {
				s += processesMessagesCurrentIndex[i] + " ";
			}
			return s;
		}
	}

	private int numberOfProcesses;
	private final int numberOfPredicates = 4;

	// Count of still running processes. The monitor starts to check predicates
	// (build lattice) whenever runningProcesses equals zero.
	private AtomicInteger runningProcesses;
	/*
	 * Q1, Q2, ..., Qn It represents the processes' queue. See distributed
	 * debugging algorithm from global state lecture!
	 */
	private List<List<Message>> processesMessages;

	// list of states
	private LinkedList<State> states;

	// The predicates checking results
	private boolean[] possiblyTruePredicatesIndex;
	private boolean[] definitelyTruePredicatesIndex;

	public Monitor(int numberOfProcesses) {
		this.numberOfProcesses = numberOfProcesses;

		runningProcesses = new AtomicInteger();
		runningProcesses.set(numberOfProcesses);

		processesMessages = new ArrayList<>(numberOfProcesses);
		for (int i = 0; i < numberOfProcesses; i++) {
			List<Message> tempList = new ArrayList<>();
			processesMessages.add(i, tempList);
		}

		states = new LinkedList<>();

		possiblyTruePredicatesIndex = new boolean[numberOfPredicates];// there
																		// are
																		// three
		// predicates
		for (int i = 0; i < numberOfPredicates; i++)
			possiblyTruePredicatesIndex[i] = false;

		definitelyTruePredicatesIndex = new boolean[numberOfPredicates];
		for (int i = 0; i < numberOfPredicates; i++)
			definitelyTruePredicatesIndex[i] = false;
	}

	/**
	 * receive messages (events) from processes
	 *
	 * @param processId
	 * @param message
	 */
	public void receiveMessage(int processId, Message message) {
		synchronized (processesMessages) {
			processesMessages.get(processId).add(message);
		}
	}

	/**
	 * Whenever a process terminates, it notifies the Monitor. Monitor only
	 * starts to build lattice and check predicates when all processes terminate
	 *
	 * @param processId
	 */
	public void processTerminated(int processId) {
		runningProcesses.decrementAndGet();
	}

	public boolean[] getPossiblyTruePredicatesIndex() {
		return possiblyTruePredicatesIndex;
	}

	public boolean[] getDefinitelyTruePredicatesIndex() {
		return definitelyTruePredicatesIndex;
	}

	@Override
	public void run() {
		// wait till all processes terminate
		
		while (runningProcesses.get() != 0)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		// create initial state (S00)
		State initialState = new State(numberOfProcesses);

		// check predicates for part (b)
		for (int predicateNo = 0; predicateNo < 3; predicateNo++) {
			System.out.printf("Predicate%d-----------------------------------\n",predicateNo);
			states.add(initialState); // add the initial state to states list
			buildLattice(predicateNo, 0, 1);
			states.clear();

		}

		if (numberOfProcesses > 2) {
			int predicateNo = 3;
			System.out.printf("Predicate%d-----------------------------------\n",predicateNo);
			states.add(initialState); // add the initial state to states list
			buildLattice(predicateNo, 0, 2);
			states.clear();
		}
		
		for(int i = 0; i < processesMessages.size(); ++i) {
			List<Message> msgs = processesMessages.get(i);
			for(int j = 0; j < msgs.size(); ++j) {
				Message msg = msgs.get(j);
				System.out.print(msg + " ");
			}
			System.out.println();
		}

	}

	public void buildLattice(int predicateNo, int process_i_id, int process_j_id) {
		// TODO
		/*
		 * - implement this function to build the lattice of consistent states.
		 * - The goal of building the lattice is to check a predicate if it is
		 * possibly or/and definitely True. Thus your function should stop
		 * whenever the predicate evaluates to both possibly and definitely
		 * True. NOTE1: this function should call findReachableStates and
		 * checkPredicate functions. NOTE2: predicateNo, process_i_id and
		 * process_j_id are described in checkPredicate function.
		 */
		
		boolean[] possiblyDefinitely = checkPredicate(predicateNo, process_i_id, process_j_id);
		possiblyTruePredicatesIndex[predicateNo] = possiblyDefinitely[0];
		definitelyTruePredicatesIndex[predicateNo] = possiblyDefinitely[1];
	}
	
	/**
	 * find all reachable states starting from a given state
	 *
	 * @param state
	 * @return list of all reachable states
	 */
	public LinkedList<State> findReachableStates(State state) {
		// TODO
		/*
		 * Given a state, implement a code that find all reachable states. The
		 * function should return a list of all reachable states
		 *
		 */
	
		LinkedList<State> reachables = new LinkedList<State>();
		
		for(int i = 0; i < numberOfProcesses; ++i) {
			int[] curIdxs = state.getProcessesMessagesCurrentIndex().clone();
			++curIdxs[i];
			if(curIdxs[i] < processesMessages.get(i).size()) {
				// Create new state;
				State s = new State(curIdxs);
				VectorClock iClock = processesMessages.get(i).get(s.getProcessMessageCurrentIndex(i)).getVectorClock();
				for(int j = 0; j < numberOfProcesses; ++j) {
					if(i == j) continue;
					VectorClock jClock = processesMessages.get(j).get(s.getProcessMessageCurrentIndex(j)).getVectorClock();
					boolean consistent = jClock.checkConsistency(i, iClock);
					//System.out.println(iClock + " " + jClock + ": " + consistent);
					if(consistent && !reachables.contains(s)) {
						reachables.add(s);
					}
				}
			}
		}
		
		return reachables;
	}

	/**
	 * - check a predicate and return true if the predicate is **definitely**
	 * True. - To simplify the code, we check the predicates only on local
	 * variables of two processes. Therefore, process_i_Id and process_j_id
	 * refer to the processes that have the local variables in the predicate.
	 * The predicate0, predicate1 and predicate2 contain the local variables
	 * from process1 and process2. whilst the predicate3 contains the local
	 * variables from process1 and process3.
	 *
	 * @param predicateNo:
	 *            which predicate to validate
	 * @return true if predicate is definitely true else return false
	 */
	private boolean[] checkPredicate(int predicateNo, int process_i_id, int process_j_id) {
		// TODO
		/*
		 * - check if a predicate is possibly and/or definitely true. - iterate
		 * over all reachable states to check the predicates. NOTE: you can use
		 * the following code switch (predicateNo) { case 0: predicate =
		 * Predicate.predicate0(process_i_Message, process_j_Message); break;
		 * case 1: ... }
		 */
		
		boolean[] possiblyDefinitely = new boolean[2];
		possiblyDefinitely[0] = false;
		possiblyDefinitely[1] = false;
		
		List<State> levelReachables = new LinkedList<State>();
		
		int[] initial = new int[this.numberOfProcesses];
		for(int i = 0; i < initial.length; ++i) initial[i] = 0;
		
		levelReachables.add(new State(initial));
		
		while(!levelReachables.isEmpty()) {
			List<State> nextLevelReachables = new LinkedList<State>();
			//System.out.println("NEW LEVEL");
			boolean allTrue = true;
			for(int i = 0; i < levelReachables.size(); ++i) {
				State s = levelReachables.get(i);
				//System.out.println(s);
				boolean predicate = false;
				switch (predicateNo) { 
				case 0: 
					predicate = Predicate.predicate0(
							processesMessages.get(process_i_id).get(s.getProcessMessageCurrentIndex(process_i_id)),
							processesMessages.get(process_j_id).get(s.getProcessMessageCurrentIndex(process_j_id))
					); break;
				case 1: 
					predicate = Predicate.predicate1(
							processesMessages.get(process_i_id).get(s.getProcessMessageCurrentIndex(process_i_id)),
							processesMessages.get(process_j_id).get(s.getProcessMessageCurrentIndex(process_j_id))
					); break;
				case 2: 
					predicate = Predicate.predicate2(
							processesMessages.get(process_i_id).get(s.getProcessMessageCurrentIndex(process_i_id)),
							processesMessages.get(process_j_id).get(s.getProcessMessageCurrentIndex(process_j_id))
					); break;
				case 3: 
					predicate = Predicate.predicate3(
							processesMessages.get(process_i_id).get(s.getProcessMessageCurrentIndex(process_i_id)),
							processesMessages.get(process_j_id).get(s.getProcessMessageCurrentIndex(process_j_id))
					); break;
				}
				allTrue = allTrue && predicate;
				possiblyDefinitely[0] = possiblyDefinitely[0] | predicate;
				
				List<State> sReach = findReachableStates(s);
				System.out.print(s + "//// ");
				for(int j = 0; j < sReach.size(); ++j) {
					System.out.print(sReach.get(j) + "// ");
					if(!nextLevelReachables.contains(sReach.get(j))) {
						nextLevelReachables.add(sReach.get(j));
					}
				}
				System.out.println();
			}
			if(allTrue) possiblyDefinitely[1] = true;
			levelReachables = nextLevelReachables;
		}
			
		
		return possiblyDefinitely;
		
	}

}
