package de.unistgt.ipvs.vs.ex1.common;

import java.util.List;
import java.util.ArrayList;

public class MessageUtils {
	
	public static String[] split(String message) {
		
		String payload;
		try {
			payload = message.split("[<>]")[1].toUpperCase();
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("No payload found");
		}
		
		String[] parts = payload.split(":");
		int dataLen;
		try {
			if(parts[0].length() == 2)
				dataLen = Integer.valueOf(parts[0]);
			else throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new RuntimeException("Length checker should be 2 zero-padded digits");
		}
		
		List<String> cmds = new ArrayList<String>();
		for(String cmd : parts[1].split(" ")) {
			if(cmd.length() == 0) continue;
			cmds.add(cmd);
		}
		
		if (dataLen == 5 + parts[1].length()) return cmds.toArray(new String[cmds.size()]);
		else throw new RuntimeException("Message not as long as declared");
	}
	
	public static String generate(String payload) {
		return String.format("<%02d:%s>", payload.length() + 5, payload);
	}
}
