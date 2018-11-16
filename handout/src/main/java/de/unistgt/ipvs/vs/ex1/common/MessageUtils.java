package de.unistgt.ipvs.vs.ex1.common;

import java.util.List;
import java.util.ArrayList;

public class MessageUtils {
	
	public static Command[] parse(String message) {
		List<Command> cmds = new ArrayList<Command>();
		String payload;
		try {
			payload = message.split("[<>]")[1].toUpperCase();
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
		
		String[] parts = payload.split(":");
		int dataLen;
		try {
			if(parts[0].length() == 2)
				dataLen = Integer.valueOf(parts[0]);
			else return null;
		} catch (NumberFormatException e) {
			return null;
		}
		
		// Accounting for <, >, XX and :
		dataLen -= 5;
		
		String curCmd = null;
		for(String particle : parts[1].split(" ")) {
			int particleLen = particle.length();
			dataLen -= particleLen + 1;
			if(particle.length() == 0) continue;
			switch(particle) {
			case "RDY": 
			case "OK":
			case "FIN":
				cmds.add(new Command(particle, null));
				break;
			case "ERR":
			case "ADD":
			case "SUB":
			case "MUL":
			case "RES":
				curCmd = particle;
				break;
			default:
				cmds.add(new Command(curCmd, particle));
			}
		}
		
		if (dataLen == -1) return cmds.toArray(new Command[cmds.size()]);
		else return null;
	}
	
	public static String generate(String payload) {
		return String.format("<%02d:%s>", payload.length() + 5, payload);
	}
}
