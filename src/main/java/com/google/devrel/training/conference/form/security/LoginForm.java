package com.google.devrel.training.conference.form.security;

public class LoginForm {

	public String emailAddress;

	public String password;

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public LoginForm() {
	}

	public LoginForm(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [emailAddress=" + emailAddress + ", password=" + password + "]";
	}
}
