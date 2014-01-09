/**
 * 
 */
package com.ldapui.service;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ldapui.connection.PersonDao;
import com.ldapui.model.Person;

/**
 * A user service that finds users in an LDAP store.
 * 
 * @author Chris
 * 
 */
@Service
public class LdapUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(LdapUserService.class);

    @Inject
    PersonDao personDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.ldapui.service.UserService#findUser(java.lang.String)
     */
    @Override
    public List<Person> findPersonByName(String name) throws ServiceException {
        try {
            List<Person> people = personDao.findByName(name);
            if (people == null) {
                return Collections.emptyList();
            }

            return people;
        } catch (Exception e) {
            throw new ServiceException("Could not find users with name " + name, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ldapui.service.UserService#canUserBind(java.lang.String)
     */
    @Override
    public boolean authenticate(String userDN, String password) throws ServiceException {
        try {
            return personDao.authenticate(userDN, password);
        } catch (Exception e) {
            throw new ServiceException("Unable to determine if user " + userDN + " is authenticated owing to "
                    + e.getMessage(), e);
        }
    }

    @Override
    public List<Person> findUsersInOrganization(String organizationId) {
        // TODO Auto-generated method stub
        return null;
    }
}
