package com.tabner.metadata;

import java.util.UUID;

public class Task1TokenGenerator {
	public static String generateToken() {
		String uuid=UUID.randomUUID().toString();
		String[] s = uuid.split("-");
		long l = System.nanoTime();
		String token = s[s.length-2] + l + s[0];
		return token;
	}
}
