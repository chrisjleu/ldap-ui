/**
 * 
 */
package com.ldapui.model;

import java.util.List;

/**
 * Represents an organization.
 * 
 * @author George
 * 
 */
public class Organization {

	private String id;
	private String name;
	private List<Organization> subOrgs;
	private List<User> users;

	// ///////////////////// GETTERS AND SETTERS /////////////////////

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Organization> getSubOrgs() {
		return subOrgs;
	}

	public void setSubOrgs(List<Organization> subOrgs) {
		this.subOrgs = subOrgs;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
