/**
 * 
 */
package com.ldapui.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * In a Servlet 3.0+ environment, you have the option of configuring the Servlet container
 * programmatically as an alternative or in combination with a web.xml file.
 * WebApplicationInitializer is an interface provided by Spring MVC that ensures your implementation
 * is detected and automatically used to initialize any Servlet 3 container. An abstract base class
 * implementation of WebApplicationInitializer named AbstractDispatcherServletInitializer makes it
 * even easier to register the DispatcherServlet by simply overriding methods to specify the servlet
 * mapping and the location of the DispatcherServlet configuration.
 * 
 * @author George
 * 
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { WebSecurityConfiguration.class, RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebMvcConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	/*
	 * @Override public void onStartup(ServletContext servletContext) throws ServletException {
	 * 
	 * // Create the root appcontext AnnotationConfigWebApplicationContext rootContext = new
	 * AnnotationConfigWebApplicationContext(); rootContext.register(RootConfig.class); // since we
	 * registered RootConfig instead of passing it to the constructor rootContext.refresh();
	 * 
	 * // Manage the lifecycle of the root appcontext servletContext.addListener(new
	 * ContextLoaderListener(rootContext)); servletContext.setInitParameter("defaultHtmlEscape",
	 * "true");
	 * 
	 * // now the config for the Dispatcher servlet AnnotationConfigWebApplicationContext mvcContext
	 * = new AnnotationConfigWebApplicationContext(); mvcContext.register(WebMvcConfig.class);
	 * 
	 * // The main Spring MVC servlet. ServletRegistration.Dynamic appServlet =
	 * servletContext.addServlet("appServlet", new DispatcherServlet( mvcContext));
	 * appServlet.setLoadOnStartup(1); Set<String> mappingConflicts = appServlet.addMapping("/");
	 * 
	 * if (!mappingConflicts.isEmpty()) { for (String s : mappingConflicts) {
	 * logger.error("Mapping conflict: " + s); } throw new
	 * IllegalStateException("'appServlet' cannot be mapped to '/' under Tomcat versions <= 7.0.14"
	 * ); } }
	 */
}
