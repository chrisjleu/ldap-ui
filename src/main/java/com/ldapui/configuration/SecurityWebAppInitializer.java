/**
 * 
 */
package com.ldapui.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * <p>
 * Spring Security relies on a Servlet filter to apply your security configuration. A filter is used so that security is
 * applied before the Spring MVC Dispatcher Servlet gets involved in processing incoming requests. The Spring Security
 * filter is referred to as the Spring Security filter chain, as it actually delegates to a chain of filters internally
 * that each apply one aspect of the security responsibility.
 * </p>
 * <p>
 * Spring provides <code>AbstractSecurityWebApplicationInitializer</code> and optionally overriding methods to customize
 * the mapping.
 * 
 * The most basic example below accepts the default mapping and adds springSecurityFilterChain with the following
 * characteristics:
 * <ul>
 * <li>springSecurityFilterChain is mapped to "/*".</li>
 * <li>springSecurityFilterChain uses the dispatch types of <code>ERROR</code> and <code>REQUEST</code>.</li>
 * <li>The springSecurityFilterChain mapping is inserted before any servlet Filter mappings that have already been
 * configured.</li>
 * </ul>
 * </p>
 * <p>
 * NB: It is important that the Spring Security setup is done before the DispatcherServlet configuration in another
 * implementation of <code>WebAppInitializer</code>. We use the <code>@Order</code> annotation to manage the order of
 * execution.
 * </p>
 * 
 * @author Chris
 * @see WebAppInitializer
 */
@Order(1)
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
    // Nothing needed here yet - the defaults provided by AbstractSecurityWebApplicationInitializer are sufficient
}
