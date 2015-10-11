package com.google.devrel.training.conference.domain.live;

public class Live {
	
	private boolean live;

	/**
	 * @return the live
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * @param live the live to set
	 */
	public void setLive(boolean live) {
		this.live = live;
	}

	public Live(boolean live) {		
		this.live = live;
	}

	public Live() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Live [live=" + live + "]";
	}
	
	

	
	
	
	

}
