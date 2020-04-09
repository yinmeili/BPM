package com.h3bpm.web.enumeration;

public interface Enumeration {

	/**
	 * Returns the value of this status.
	 * 
	 * @return The string, never be blank.
	 */
	public String getValue();

	/**
	 * Returns the display name of this status.
	 * 
	 * @return The string, never be blank.
	 */
	public String getDisplayName();
}
