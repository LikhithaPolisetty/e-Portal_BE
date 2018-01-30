package com.tabner.metadata;

import java.util.HashSet;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
	HashSet<String> cache = new HashSet<String>();
	
	
	public void saveToken(String token)
	{
		cache.add(token);			
	}
	
	
	public boolean removeToken(String token)
	{
		if(cache.contains(token))
		{
			cache.remove(token);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean validateToken(String token) {
		  if (cache.contains(token)) {
			  System.out.println("has token " + cache);
			  System.out.println(token);
		   return true;
		  } else {
			  System.out.println("has no token" + cache);
			  System.out.println(token);
		   return false;
		  }
		 }
	
	
}
