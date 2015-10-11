package com.google.devrel.training.conference.spi.security;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import java.security.MessageDigest;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.form.security.LoginForm;
import com.google.devrel.training.conference.form.security.SignupForm;
import com.googlecode.objectify.Key;
import com.google.devrel.training.conference.authenticator.MyAuthenticator;
import com.google.devrel.training.conference.domain.security.*;

/**
 * Defines Security APIs.
 */
@Api(name = "security", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, authenticators = {
				MyAuthenticator.class }, description = "API for the security")

public class SecurityApi {

	@ApiMethod(name = "signup", path = "signup", httpMethod = HttpMethod.POST)
	public Account signup(final SignupForm signupForm) throws BadRequestException {

		if (signupForm.emailAddress == null || signupForm.password == null) {
			throw new BadRequestException("Missing Parameters");
		}

		// TODO
		// Ckeck if the user isn't a robot recaptcha

		// Check unique account (emailAddress)
		Account uniqueAccount = ofy().load().type(Account.class).filter("emailAddress", signupForm.emailAddress).first()
				.now();

		if (uniqueAccount != null) {
			throw new BadRequestException("This email is already used");
		}

		// Create new account
		String passwordEncrypted;
		try {
			passwordEncrypted = hashText(signupForm.password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestException("Error server : hash password error");
		}
		Account account = new Account(signupForm.emailAddress, passwordEncrypted);

		// Save the entity in the datastore
		ofy().save().entity(account).now();

		return account;

	}

	@ApiMethod(name = "signin", path = "signin", httpMethod = HttpMethod.POST)
	public Token signin(final LoginForm loginForm) throws BadRequestException, UnauthorizedException {

		if (loginForm.emailAddress == null || loginForm.password == null) {
			throw new BadRequestException("Missing Parameters");
		}

		// Find the user
		Account account = ofy().load().type(Account.class).filter("emailAddress", loginForm.emailAddress).first().now();

		// Check the account if exists and it's a good login/password
		if (account == null) {
			throw new BadRequestException("No account with this email");
		} else {

			String passwordEncrypted;
			try {
				passwordEncrypted = hashText(loginForm.password);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BadRequestException("Error server : hash password error");
			}

			if (!(account.getPassword().equals(passwordEncrypted))) {
				throw new BadRequestException("Bad login/password");
			}

		}

		// Generate the token
		String authtoken = account.createToken();
		// save the token in the datastore
		ofy().save().entity(account).now();

		// cast authtoken to Token class
		// because App Engine doesn't support "String" return
		Token token = new Token(authtoken);

		return token;
	}

	@ApiMethod(name = "account", path = "account", httpMethod = HttpMethod.GET)
	public Account account(final User user) throws UnauthorizedException, BadRequestException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		// Find the user
		Key<Account> key = Key.create(Account.class, Long.valueOf(user.getId()));
		Account account = (Account) ofy().load().key(key).now();
		if (account == null) {
			throw new BadRequestException("No account with this email");
		}

		return account;
	}

	@ApiMethod(name = "logout", path = "logout", httpMethod = HttpMethod.GET)
	public Account logout(final User user) throws UnauthorizedException, BadRequestException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		// Find the user
		Key<Account> key = Key.create(Account.class, Long.valueOf(user.getId()));
		Account account = (Account) ofy().load().key(key).now();
		if (account == null) {
			throw new BadRequestException("No account with this email");
		}

		// Delete the authToken
		account.setAuthToken("");
		ofy().save().entity(account).now();

		return account;
	}

	private static String convertByteToHex(byte data[]) {
		StringBuffer hexData = new StringBuffer();
		for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
			hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));

		return hexData.toString();
	}

	private static String hashText(String textToHash) throws Exception {
		final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
		sha512.update(textToHash.getBytes());

		return convertByteToHex(sha512.digest());
	}

}
