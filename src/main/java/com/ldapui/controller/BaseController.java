/**
 * 
 */
package com.ldapui.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ldapui.model.User;
import com.ldapui.service.ServiceException;
import com.ldapui.service.UserService;

/**
 * This is the entry point to the application. Notice that it is just a Plain-old Java Object (POJO) with annotations.
 * By using the @RequestMapping("/somevalue") we can map URLs to methods of this class.
 * 
 * @author George
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

        User user = userService.findUser("10001");
        userService.findUserRoles("10001");

        model.addAttribute("message", "Welcome " + user.getFirstName() + " to LDAP UI.");

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

    // /////////// FUNCTIONAL CALLS (Could resppond in JSON) ///////////////////////

    @RequestMapping(value = { "/usersearch/{userId}" }, method = RequestMethod.GET)
    public String usersearch(@PathVariable String userId, ModelMap model) {

        User user = userService.findUser(userId);
        if (user == null) {
            model.addAttribute("message", "Could not find user with ID " + userId);
        } else {
            model.addAttribute("message", "Found" + user.getEmailAddress());
        }

        return "welcome";
    }


    /**
     * GET requests will just be directed back to the dashboard view.
     * 
     * @return
     */
    @RequestMapping(value = { "/bindcheck" }, method = RequestMethod.GET)
    public String canUserBindGet() {
        return "dashboard";
    }

    
    @RequestMapping(value = { "/bindcheck" }, method = RequestMethod.POST)
    public String canUserBind(@RequestParam("dn") String dn, @RequestParam("pwd") String password, ModelMap model) {
        boolean isBound = false;
        try {
            isBound = userService.canUserBind(dn, password);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        model.addAttribute("message", "Is user \"" + dn + "\" bound? " + isBound);

        return "dashboard";
    }

}
