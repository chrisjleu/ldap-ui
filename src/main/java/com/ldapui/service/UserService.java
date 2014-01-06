/**
 * 
 */
package com.ldapui.service;

import java.util.List;

import com.ldapui.model.Organization;
import com.ldapui.model.Role;
import com.ldapui.model.User;

/**
 * This is intended to the main interface for accessing user information. Classes that make use of
 * this interface are shielded from the actual data source (it could be a databse, file or LDAP
 * repository for instance).
 * 
 * @author George
 * 
 */
public interface UserService {

	/**
	 * Locates a user based on the user id.
	 * 
	 * @return The {@link User}.
	 */
	public User findUser(String userId);

	/**
	 * Finds all users in the given organisation.
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<User> findUsersInOrganization(String organizationId);

	/**
	 * Locates the roles for a given user.
	 * 
	 * @param userId
	 *            The user ID.
	 * @return A list of {@link Role} objects for this user.
	 */
	public List<Role> findUserRoles(String userId);

	/**
	 * Finds the sub-organizations of an organization.
	 * 
	 * @param organizationId
	 *            The ID of the organization.
	 * @return A list of {@link Organization}s that are sub organizations of the given organization.
	 */
	public List<Organization> findSubOrganizations(String organizationId);

	/**
	 * Determines whether a user can bind. Sometimes, in the world of LDAP, there are restrictions
	 * even if the user exists and valid credentials are provided. TODO Method should take a
	 * password as well
	 * 
	 * @param userDN
	 * @param password
	 * @return True if the user can bind, false otherwise.
	 * @throws ServiceException
	 *             Thrown if there is any problem connecting with the data source.
	 */
	public boolean canUserBind(String userDN, String password) throws ServiceException;
}
