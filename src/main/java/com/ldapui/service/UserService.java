/**
 * 
 */
package com.ldapui.service;

import java.util.List;

import com.ldapui.model.Person;

/**
 * This is intended to the main interface for accessing user information. Classes that make use of this interface are
 * shielded from the actual data source (it could be a databse, file or LDAP repository for instance).
 * 
 * @author Chris
 * 
 */
public interface UserService {

    /**
     * Finds all users with the given name.
     * 
     * @param name
     *            The name of the user.
     * @return All users with the given name or an empty list if no users match.
     * @throws ServiceException
     *             Thrown if there is any problem connecting with the data source.
     */
    List<Person> findPersonByName(String name) throws ServiceException;

    /**
     * Finds all users in the given organisation.
     * 
     * @param organizationId
     * @return
     */
    public List<Person> findUsersInOrganization(String organizationId);

    /**
     * Determines whether a user can bind. Sometimes, in the world of LDAP, there are restrictions even if the user
     * exists and valid credentials are provided.
     * 
     * @param userDN
     * @param password
     * @return True if the user can bind, false otherwise.
     * @throws ServiceException
     *             Thrown if there is any problem connecting with the data source.
     */
    public boolean authenticate(String userDN, String password) throws ServiceException;
}
