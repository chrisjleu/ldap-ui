package com.ldapui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.ldap.core.ContextSource;
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

    @Value("${useJNDIDatasource}")
    boolean useJNDIDatasource;

    @Value("${jndi-datasource}")
    String datasourceJndiName;

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

    /**
     * This is used to make requests to an LDAP server.
     * 
     * @return
     */
    @Bean
    public LdapTemplate ldapTemplate() {
        if (useJNDIDatasource) {
            return new LdapTemplate((ContextSource) jndiLdapContextSource());
        } else {
            return new LdapTemplate(ldapContextSource());
        }
    }

    /**
     * The LDAP credentials in this case are provided by some external source, probably configured in the application
     * server. Note that this bean is not initialized (as is the default with beans in Spring) unless it is used.
     * 
     * @return
     */
    @Bean
    @Lazy(true)
    public JndiObjectFactoryBean jndiLdapContextSource() {
        JndiObjectFactoryBean dataSource = new JndiObjectFactoryBean();
        dataSource.setJndiName(datasourceJndiName);
        return dataSource;
    }

    @Bean
    @Lazy(true)
    public LdapContextSource ldapContextSource() {
        LdapContextSource cs = new LdapContextSource();
        cs.setUrl(url);
        cs.setUserDn(login);
        cs.setPassword(password);
        cs.setBase(base);
        cs.setDirObjectFactory(new DefaultDirObjectFactory().getClass());
        cs.setAnonymousReadOnly(anonymousReadOnly);
        return cs;
    }
}