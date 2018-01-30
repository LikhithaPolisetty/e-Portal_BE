package com.tabner.metadata;

import java.util.UUID;

public class TokenGenerator {
	
	public static String generateToken() {
		String uuid=UUID.randomUUID().toString();
		String uuid1=UUID.randomUUID().toString();
		String[] s = uuid.split("-");
		String[] s1 = uuid1.split("-");
		long l = System.nanoTime();
		String token = s[s.length-2] + l + s1[s1.length-3] + l + s[0];
		return token;
	}
}
