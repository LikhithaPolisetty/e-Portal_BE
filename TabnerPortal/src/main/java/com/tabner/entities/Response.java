package com.tabner.entities;


/*
 * this is a pojo class which is used to send the responses from this application to the front end React application
 */


public class Response {
	private Object Response;
	private Object verified;
	private Object token;
	private Object secret;
	private Object tfa;
	
	
	public Object getVerified() {
		return verified;
	}

	public void setVerified(Object verified) {
		this.verified = verified;
	}

	public Object getTfa() {
		return tfa;
	}

	public void setTfa(Object tfa) {
		this.tfa = tfa;
	}

	public Object getSecret() {
		return secret;
	}

	public void setSecret(Object secret) {
		this.secret = secret;
	}

	public Object getResponse() {
		return this.Response;
	}

	public void setResponse(Object response) {
		this.Response = response;
	}

	public Object getToken() {
		return this.token;
	}

	public void setToken(Object token) {
		this.token = token;
	}
}
