/**
 * 
 */
package com.ldapui.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The <code>@EnableWebSecurity</code> annotation and <code>WebSecurityConfigurerAdapter</code> work
 * together to provide web based security. By extending <code>WebSecurityConfigurerAdapter</code>
 * and only a few lines of code we are able to do the following:
 * <ul>
 * <li>Require the user to be authenticated prior to accessing any URL within our application</li>
 * <li>Create a user with the username "user", password "password", and role of "ROLE_USER"</li>
 * <li>Enables HTTP Basic and Form based authentication</li>
 * <li>Spring Security will automatically render a login page and logout success page for you.</li>
 * </ul>
 * 
 * @author George
 * 
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth
	      .inMemoryAuthentication()
	        .withUser("user").password("password").roles("USER");
	  }
}
