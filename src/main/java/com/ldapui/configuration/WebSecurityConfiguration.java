/**
 * 
 */
package com.ldapui.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * The <code>@EnableWebSecurity</code> annotation and <code>WebSecurityConfigurerAdapter</code> work together to provide
 * web based security. By extending <code>WebSecurityConfigurerAdapter</code> and only a few lines of code we are able
 * to do the following:
 * <ol>
 * <li>Allow in memory authentication with a user named "user"</li>
 * <li>Allow in memory authentication with an administrative user named "admin"</li>
 * <li>Ignore any request that starts with "/resources/". This is similar to configuring http@security=none when using
 * the XML namespace configuration.</li>
 * <li>Allow anyone (including unauthenticated users) to access to the URLs "/welcome" and "/about"</li>
 * <li>Allow anyone (including unauthenticated users) to access to the URLs "/login" and "/login?error". The permitAll()
 * in this case means, allow access to any URL that formLogin() uses.</li>
 * <li>Any URL that starts with "/admin/" must be an administrative user. For our example, that would be the user
 * "admin".</li>
 * <li>All remaining URLs require that the user be successfully authenticated.</li>
 * <li>Setup form based authentication using the Java configuration defaults. Authentication is performed when a POST is
 * submitted to the URL "/login" with the parameters "username" and "password".</li>
 * <li>Explicitly state the login page, which means the developer is required to render the login page when GET /login
 * is requested.</li>
 * <li>The URL to redirect to after logout has occurred by default is "/login?logout". This changes that so that it is
 * easier to map in our Spring MVC controller class.</li>
 * </ol>
 * <p>
 * NB: This class is not enough to start the Spring Security filter chain. This should be configured in another
 * implementation of {@link WebApplicationInitializer}.
 * </p>
 * <p>
 * See Spring Security tutorials <a
 * href="http://spring.io/blog/2013/07/03/spring-security-java-config-preview-web-security/">one</a> and <a
 * href="http://spring.io/guides/tutorials/web/6/">two</a> for more information.
 * </p>
 * 
 * @author Chris
 * @see AbstractSecurityWebApplicationInitializer
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .inMemoryAuthentication()
          .withUser("user")  // 1
            .password("password")
            .roles("USER")
            .and()
          .withUser("admin") // 2
            .password("password")
            .roles("ADMIN","USER");
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/resources/**"); // 3
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .csrf().disable() // TODO enable at some point http://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection
        .authorizeRequests()
          .antMatchers("/", "/welcome", "/logout").permitAll() // 4
          .antMatchers("/admin/**").hasRole("ADMIN") // 6
          .anyRequest().authenticated() // 7
          .and()
      .formLogin()  // 8
          .loginPage("/login") // 9
          .permitAll() // 5
          .and()
          .logout().logoutSuccessUrl("/welcome"); // 10
    }
}
