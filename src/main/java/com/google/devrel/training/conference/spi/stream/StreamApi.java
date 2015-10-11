package com.google.devrel.training.conference.spi.stream;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import java.util.List;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.authenticator.MyAuthenticator;
import com.google.devrel.training.conference.domain.security.Account;
import com.google.devrel.training.conference.domain.stream.Stream;
import com.google.devrel.training.conference.form.stream.StreamForm;
import com.googlecode.objectify.Key;

/**
 * Defines Stream APIs.
 */
@Api(name = "stream", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, authenticators = {
				MyAuthenticator.class }, description = "API for the streams")
public class StreamApi {

	@ApiMethod(name = "listStream")
	public List<Stream> listStream(final User user) throws UnauthorizedException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		// key of user
		Key<Account> accountKey = Key.create(Account.class, Long.valueOf(user.getId()));

		// Get all streams of user
		List<Stream> streams = ofy().load().type(Stream.class).ancestor(accountKey).list();

		return streams;
	}

	@ApiMethod(name = "getStream")
	public Stream getStream(final User user, @Named("websafeKey") String websafeKey) throws NotFoundException, UnauthorizedException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Stream> key = Key.create(websafeKey);
		Stream stream = (Stream) ofy().load().key(key).now();

		// Not found
		if (stream == null) {
			throw new NotFoundException("No stream found with key: " + websafeKey);
		}

		// Check if it's owner
		Key<Account> accountKey = Key.create(Account.class, Long.valueOf(user.getId()));
		if (!(stream.getAccountKey().equivalent(accountKey))) {
			throw new UnauthorizedException("Authorization required -> Not owner");
		}

		return stream;
	}

	@ApiMethod(name = "insertStream")
	public Stream insertStream(final User user, StreamForm streamForm)
			throws NotFoundException, UnauthorizedException, BadRequestException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Account> accountKey = Key.create(Account.class, Long.valueOf(user.getId()));

		// Allocate a key for the stream -- let App Engine allocate the ID
		// Don't forget to include the parent Profile in the allocated ID
		final Key<Stream> streamKey = ofy().factory().allocateId(accountKey, Stream.class);

		// Get the Conference Id from the Key
		final Long streamId = streamKey.getId();

		// Create a new Stream Entity
		Stream stream = new Stream(streamId, user, streamForm);

		// Save Conference and Profile Entities
		ofy().save().entity(stream).now();

		return stream;
	}

	@ApiMethod(name = "removeStream")
	public void removeStream(final User user, @Named("id") String id) throws NotFoundException, UnauthorizedException {

		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Account> accountKey = Key.create(Account.class, Long.valueOf(user.getId()));
		String websafeKey = Key.create(accountKey, Stream.class, Long.valueOf(id)).getString();
		Key<Stream> key = Key.create(websafeKey);
		ofy().delete().key(key).now();

	}

}
