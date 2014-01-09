package com.ldapui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * The root configuration where most (Spring-managed) objects specific to this application should be initialized.
 */
@Configuration
@ComponentScan(basePackages = { "com.ldapui.service", "com.ldapui.connection" })
@PropertySource("classpath:/com/ldapui/configuration/ldap/ldap-connection.properties")
public class RootConfig {

    /**
     * Must be declared so that the properties file can be picked up and used in combination with the
     * <code>@Value</code> annotation.
     * 
     * @return {@link PropertySourcesPlaceholderConfigurer}
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Value("${url}")
    String url;

    @Value("${loginDN}")
    String login;

    @Value("${password}")
    String password;

    @Value("${base}")
    String base;

    @Value("${anonymousReadOnly}")
    boolean anonymousReadOnly;

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource cs = new LdapContextSource();
        cs.setUrl(url);
        cs.setUserDn(login);
        cs.setPassword(password);
        cs.setBase(base);
        cs.setDirObjectFactory(new DefaultDirObjectFactory().getClass ());
        cs.setAnonymousReadOnly(anonymousReadOnly);
        return cs;
    }
}