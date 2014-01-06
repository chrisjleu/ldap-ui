/**
 * 
 */
package com.ldapui.model;

/**
 * Represents a user.
 * 
 * @author George
 * 
 */
public class User {

	private String id;
	private String firstName;
	private String lastName;
	private String emailAddress;

	// ////////////// GETTERS AND SETTERS //////////////////////

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
