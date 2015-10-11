package com.google.devrel.training.conference.domain.security;

public class Token {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Token() {
	}

	public Token(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + "]";
	}

}
