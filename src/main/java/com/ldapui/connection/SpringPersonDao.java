package com.ldapui.connection;

import java.util.List;

import javax.inject.Inject;
import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
import org.springframework.stereotype.Repository;

import com.ldapui.model.Person;

@Repository
public class SpringPersonDao implements PersonDao {

    private static final Logger logger = LoggerFactory.getLogger(SpringPersonDao.class);

    @Inject
    private LdapTemplate ldapTemplate;

    public void create(Person person) {
        DirContextAdapter context = new DirContextAdapter(buildDn(person));
        mapToContext(person, context);
        ldapTemplate.bind(context);
    }

    public void update(Person person) {
        Name dn = buildDn(person);
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        mapToContext(person, context);
        ldapTemplate.modifyAttributes(context);
    }

    public void delete(Person person) {
        ldapTemplate.unbind(buildDn(person));
    }

    public boolean authenticate(String cn, String password) {
        Filter filter = new WhitespaceWildcardsFilter("cn", cn);
        CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
        boolean result = ldapTemplate.authenticate("", filter.toString(), password, errorCallback);
        if (!result) {
            Exception error = errorCallback.getError();
            System.out.println(error);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Person> findByName(String name) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new WhitespaceWildcardsFilter("cn", name));
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, filter.encode(), getContextMapper());
    }

    public Person findByPrimaryKey(String name, String company, String country) {
        Name dn = buildDn(name, company, country);
        return (Person) ldapTemplate.lookup(dn, getContextMapper());
    }

    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH, filter.encode(), getContextMapper());
    }

    // //////////// Methods used by this class /////////////////////

    protected ContextMapper getContextMapper() {
        return new PersonContextMapper();
    }

    protected Name buildDn(Person person) {
        return buildDn(person.getFullName(), person.getCompany(), person.getCountry());
    }

    protected Name buildDn(String fullname, String company, String country) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("c", country);
        dn.add("ou", company);
        dn.add("cn", fullname);
        return dn;
    }

    protected void mapToContext(Person person, DirContextOperations context) {
        context.setAttributeValues("objectclass", new String[] { "top", "person" });
        context.setAttributeValue("cn", person.getFullName());
        context.setAttributeValue("sn", person.getLastName());
        context.setAttributeValue("description", person.getDescription());
    }

    private static class PersonContextMapper extends AbstractContextMapper {
        public Object doMapFromContext(DirContextOperations context) {
            Person person = new Person();
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setDescription(context.getStringAttribute("description"));
            return person;
        }
    }
}