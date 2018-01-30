package com.tabner.entities;

/*
 * this is a pojo class whose objects will be sent to this application from the front end React application through RestFul calls 
 */
public class User {

	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
