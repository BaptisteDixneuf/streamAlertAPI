package com.google.devrel.training.conference.form.stream;

import java.util.Date;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.devrel.training.conference.domain.security.Account;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class StreamForm {
	
	private String plateform;

	private String channel;

	
    

	/**
	 * @return the plateform
	 */
	public String getPlateform() {
		return plateform;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}


	public StreamForm( String plateform, String channel) {		
		this.plateform = plateform;
		this.channel = channel;		
	}

	public StreamForm() {}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StreamForm [plateform=" + plateform + ", channel=" + channel + "]";
	}

	
	
	
	
    
    
    
}
