package com.ldapui.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * This is the Web MVC related Spring configuration.
 * 
 * @author George
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.ldapui.controller" })
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

	@Bean
	public ViewResolver viewResolver() {
		if (logger.isDebugEnabled()) {
			logger.debug("setting up view resolver");
		}

		// This will ease the process of forwarding a request to a JSP. By specifiying the prefix
		// and the suffix, we can redirect to a JSP without having to code the folder or the suffix.
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
}
