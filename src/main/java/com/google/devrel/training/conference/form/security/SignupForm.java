package com.google.devrel.training.conference.form.security;

public class SignupForm {

	public String emailAddress;

	public String password;

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public SignupForm() {
	}

	public SignupForm(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	@Override
	public String toString() {
		return "SignupForm [emailAddress=" + emailAddress + ", password=" + password + "]";
	}
}
