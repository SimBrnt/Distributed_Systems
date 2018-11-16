package de.unistgt.ipvs.vs.ex1.common;

public class Command {
	public final String operation;
	public final String operator;
	public Command(String operation, String operator) {
		this.operation = operation;
		this.operator = operator;
	}
	@Override
	public String toString() {
		return operation + " " + operator;
	}
	@Override
	public boolean equals(Object other) {
		Command o = (Command) other;
		return this.operation.equals(o.operation) && this.operator.equals(o.operator);
	}
}
