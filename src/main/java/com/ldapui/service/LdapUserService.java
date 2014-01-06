/**
 * 
 */
package com.ldapui.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ldapui.connection.ConnectionBinder;
import com.ldapui.model.Organization;
import com.ldapui.model.Role;
import com.ldapui.model.User;
import com.novell.ldap.LDAPException;

/**
 * A user service that finds users in an LDAP store.
 * 
 * @author George
 * 
 */
@Service
public class LdapUserService implements UserService {

	@Inject
	ConnectionBinder connectionBinder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ldapui.service.UserService#findUser(java.lang.String)
	 */
	@Override
	public User findUser(String userId) {
		// TODO needs to go to LDAP at this point
		if ("10001".equals(userId)) {
			User george = new User();
			george.setEmailAddress("gleung@gmail.com");
			george.setId("10001");
			george.setFirstName("george");
			george.setLastName("leung");
			return george;
		}

		return null;
	}

	@Override
	public List<Role> findUserRoles(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ldapui.service.UserService#canUserBind(java.lang.String)
	 */
	@Override
	public boolean canUserBind(String userDN, String password) throws ServiceException {
		try {
			return connectionBinder.canUserBind(userDN, password);
		} catch (UnsupportedEncodingException | LDAPException | InterruptedException e) {
			throw new ServiceException("Unable to bind user " + userDN, e);
		}
	}

	@Override
	public List<User> findUsersInOrganization(String organizationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Organization> findSubOrganizations(String organizationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
