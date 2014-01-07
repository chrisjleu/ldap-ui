/**
 * 
 */
package com.ldapui.configuration;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * <p>
 * In a Servlet 3.0+ environment, you have the option of configuring the Servlet container programmatically as an
 * alternative or in combination with a web.xml file. WebApplicationInitializer is an interface provided by Spring MVC
 * that ensures your implementation is detected and automatically used to initialize any Servlet 3 container. (You are
 * free to package these implementations within your application as you see fit. Typical applications will likely
 * centralize all container initialization within a single WebApplicationInitializer so although you can have many
 * implementations, a single one is more likely - that is of course unless you are using Spring Security in which case
 * you might want to factor out the initialization of this into another class.)
 * </p>
 * <p>
 * In addition, Spring provides an abstract base class implementation of <code>WebApplicationInitializer</code> named
 * <code>AbstractDispatcherServletInitializer</code> makes it even easier to register the DispatcherServlet by simply
 * overriding methods to specify the servlet mapping and the location of the DispatcherServlet configuration.
 * </p>
 * 
 * @author Chris
 * 
 */
@Order(2)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebSecurityConfiguration.class, RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebMvcConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        return new Filter[] { characterEncodingFilter };
    }
}
