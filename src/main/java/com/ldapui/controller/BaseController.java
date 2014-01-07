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
        if(logger.isDebugEnabled()) {
            logger.debug("Handling request for the home page");
        }

        User user = userService.findUser("10001");
        userService.findUserRoles("10001");

        model.addAttribute("message", "Welcome " + user.getFirstName() + " to your Maven Web Project with Spring 3 MVC");

        return "welcome";

    }


    /**
     * Directs the user to the primary login page.
     * 
     * @return
     */
    @RequestMapping(value = { "/login" })
    public String login() {

        if(logger.isDebugEnabled()) {
            logger.debug("Directing user to main login JSP");
        }

        return "login";
    }

    /**
     * Directs the user to the primary login page.
     * 
     * @return
     */
    /*@RequestMapping(value = { "/login?error" })
    public String loginError() {

        if(logger.isDebugEnabled()) {
            logger.debug("User was not authenticated");
        }

        return "login";
    }*/

    /**
     * Directs the user to the logout page.
     * 
     * @return
     */
/*    @RequestMapping(value = { "/login?logout" })
    public String logout() {

        if(logger.isDebugEnabled()) {
            logger.debug("Directing user to the logout page");
        }

        return "welcome";
    }*/

    @RequestMapping(value = { "/usersearch/{userId}" }, method = RequestMethod.GET)
    public String usersearch(@PathVariable String userId, ModelMap model) {

        User user = userService.findUser(userId);
        if (user == null) {
            model.addAttribute("message", "Could not find user with ID " + userId);
        } else {
            model.addAttribute("message", "Foun" + user.getEmailAddress());
        }

        return "welcome";
    }

    // TODO Should be a POST method (and secure at that)
    @RequestMapping(value = { "/bindcheck/{dn}/{pwd}" }, method = RequestMethod.GET)
    public String canUserBind(@PathVariable String dn, @PathVariable String pwd, ModelMap model) {
        boolean isBound = false;
        try {
            isBound = userService.canUserBind(dn, pwd);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("message", "Is user bound? " + isBound);
        return "welcome";
    }

}
