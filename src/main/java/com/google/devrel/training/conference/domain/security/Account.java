package com.google.devrel.training.conference.domain.security;

import java.util.UUID;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Account {

	@Id
	private Long id;

	@Index
	private String emailAddress;

	@Index
	private String password;

	@Index
	private String authToken;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@SuppressWarnings("unused")
	private Account() {
	}

	public Account(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	/*
	 * public Account(final long id, String emailAddress, String password) {
	 * this.id = id; this.emailAddress = emailAddress; this.password = password;
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Account [id=" + id + ", emailAddress=" + emailAddress + ", password=" + password + ", authToken="
				+ authToken + "]";
	}

	public String createToken() {
		authToken = UUID.randomUUID().toString().replaceAll("-", "");
		return authToken;
	}

}
