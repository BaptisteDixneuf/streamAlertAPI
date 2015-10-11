package com.google.devrel.training.conference.authenticator;


import static com.google.devrel.training.conference.service.OfyService.ofy;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.devrel.training.conference.domain.security.*;


//Custom Authenticator class
public class MyAuthenticator implements Authenticator {
	@Override
	public User authenticate(HttpServletRequest request) {
		System.out.println("Token : "+ request.getHeader("token"));
		String token = request.getHeader("token");
		if (token != null) {
			// Find the email associate to the token

			Account account = ofy().load().type(Account.class).filter("authToken", token).first().now();

			if (account == null) {
				System.out.println("AUTH REFUS 1: No account");
				return null;
			} else {
				String id = String.valueOf(account.getId());
				System.out.println("User id  : "+ id + ", User email: " + account.getEmailAddress());
				return new User(id, account.getEmailAddress());
			}

		}
		System.out.println("AUTH REFUS 2: No token");
		return null;
	}
}