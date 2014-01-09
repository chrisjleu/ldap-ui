/**
 * 
 */
package com.ldapui.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ldapui.model.Person;
import com.ldapui.service.ServiceException;
import com.ldapui.service.UserService;

/**
 * This is the entry point to the application. Notice that it is just a Plain-old Java Object (POJO) with annotations.
 * By using the @RequestMapping("/somevalue") we can map URLs to methods of this class.
 * 
 * @author Chris
 * 
 */
@Controller
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Inject
    UserService userService;

    /**
     * The welcome page of the web application.
     * 
     * @param model
     * @return The name of the JSP that you want to forward to. Note that forwarding does not mean redirecting (the
     *         latter involves the browser).
     */
    @RequestMapping(value = { "/", "/welcome", "/logout" }, method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        if (logger.isDebugEnabled()) {
            logger.debug("Handling request for the home page");
        }

        return "welcome";

    }

    /**
     * Directs the user to the primary login page.
     * 
     * @return
     */
    @RequestMapping(value = { "/login" })
    public String login() {
        if (logger.isDebugEnabled()) {
            logger.debug("Directing user to main login JSP");
        }

        return "login";
    }

    /**
     * Directs the user to the dashboard page. This is the page the user can do most of the useful things. They should
     * be logged in to see this page.
     * 
     * @return
     */
    @RequestMapping(value = { "/dashboard" })
    public String dashboard() {
        if (logger.isDebugEnabled()) {
            logger.debug("Directing user to the dashboard JSP");
        }

        return "dashboard";
    }

    // /////////// FUNCTIONAL CALLS ///////////////////////
    // TODO All of these could/should be JSON responses

    /**
     * Redirect any GET requests back to the dashboard page. Only POST requests are permitted.
     * 
     * @return The name of the dashboard page JSP. (without the .jsp suffix)
     */
    @RequestMapping(value = { "/person" }, method = RequestMethod.GET)
    public String personSearchByName() {
        return "dashboard";
    }

    @RequestMapping(value = { "/person" }, method = RequestMethod.POST)
    public String personSearchByName(@RequestParam("name") String name, ModelMap model) {

        List<Person> people;
        try {
            people = userService.findPersonByName(name);
            if (people.size() < 1) {
                model.addAttribute("message", "Could not find user with ID " + name);
            } else {
                StringBuilder messageBuilder = new StringBuilder();
                for (Person person : people) {
                    messageBuilder.append(person);
                    messageBuilder.append("<br/>");
                }

                model.addAttribute("message", "<b>Found the following users:</b><br/>" + messageBuilder.toString()
                        + "<br/>");
            }
        } catch (ServiceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Unable to find user with name " + name, e);
            }
            model.addAttribute("message", "Unable to find user with name " + name + ": " + e);
        }

        return "dashboard";
    }

    /**
     * Redirect any GET requests back to the dashboard page. Only POST requests are permitted.
     * 
     * @return The name of the dashboard page JSP. (without the .jsp suffix)
     */
    @RequestMapping(value = { "/bindcheck" }, method = RequestMethod.GET)
    public String canUserBindGet() {
        return "dashboard";
    }

    @RequestMapping(value = { "/bindcheck" }, method = RequestMethod.POST)
    public String canUserBind(@RequestParam("dn") String dn, @RequestParam("pwd") String password, ModelMap model) {
        boolean isBound = false;
        try {
            isBound = userService.authenticate(dn, password);
            model.addAttribute("message", "Is user \"" + dn + "\" bound? " + isBound);
        } catch (ServiceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Unable to determine if user " + dn + " can bind", e);
            }
            model.addAttribute("message", e);
        }

        return "dashboard";
    }

}
