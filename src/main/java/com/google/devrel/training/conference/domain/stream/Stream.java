package com.google.devrel.training.conference.domain.stream;

import java.util.Date;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.api.server.spi.response.BadRequestException;
import com.google.devrel.training.conference.domain.Conference;
import com.google.devrel.training.conference.domain.security.Account;
import com.google.devrel.training.conference.form.stream.StreamForm;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Stream {

	@Id
	private Long id;

	@Index
	private String plateform;

	@Index
	private String channel;

	@SuppressWarnings("unused")
	private Date creationDate;

	/**
	 * Holds Profile key as the parent.
	 */
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Account> accountKey;

	/**
	 * The userId of the organizer.
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	@Index
	private String owner;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the plateform
	 */
	public String getPlateform() {
		return plateform;
	}

	/**
	 * @param plateform
	 *            the plateform to set
	 */
	public void setPlateform(String plateform) {
		this.plateform = plateform;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @return the accountKey
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<Account> getAccountKey() {
		return accountKey;
	}

	/**
	 * @return the owner
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getOwner() {
		return owner;
	}
	
	// Get a String version of the key
		public String getWebsafeKey() {
			return Key.create(accountKey, Stream.class, id).getString();
		}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Stream(final Long id, final User user, final StreamForm streamform) throws BadRequestException {
		this.id = id;
		this.accountKey = Key.create(Account.class, Long.valueOf(user.getId()));
		this.owner = user.getId();
		updateWithStreamForm(streamform);
	}

	public Stream(Long id, String plateform, String channel, Date creationDate, Key<Account> accountKey, String owner) {
		this.id = id;
		this.plateform = plateform;
		this.channel = channel;
		this.creationDate = creationDate;
		this.accountKey = accountKey;
		this.owner = owner;
	}

	public Stream() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Stream [id=" + id + ", plateform=" + plateform + ", channel=" + channel + ", creationDate="
				+ creationDate + ", accountKey=" + accountKey + ", owner=" + owner + "]";
	}

	private void updateWithStreamForm(StreamForm streamform) throws BadRequestException {
		checkStream(streamform);
		this.channel = streamform.getChannel();
		this.plateform = streamform.getPlateform();
		this.creationDate = new Date();
	}

	private void checkStream(StreamForm streamform) throws BadRequestException {

		if (streamform.getPlateform() == null && streamform.getChannel() == null) {
			throw new BadRequestException("Missing all parameters");
		}

		if (streamform.getPlateform() == null) {
			throw new BadRequestException("Missing parameter: plateform (String) ");
		}

		if (streamform.getChannel() == null) {
			throw new BadRequestException("Missing parameter: channel (String) ");
		}

		// TODO ENUM des plateformes supportés
		// TODO Vérification longeur chaine
	}

}
