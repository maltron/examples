package net.nortlam.event.registration.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;

import net.nortlam.event.registration.util.EventRegistrationCommonController;

@ManagedBean(name = "login")
@ViewScoped
public class LoginController extends EventRegistrationCommonController 
                                                    implements Serializable {

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    @EJB
    private Service service;

    private String requestedURI;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void login() {
        LOG.log(Level.INFO, ">>> login() RequestedURI:{0}", requestedURI);
        try {
            // LOGIN LOGIN LOGIN LOGIN LOGIN LOGIN LOGIN LOGIN 
            getServletRequest().login(getEmail(), getPassword());

            // SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS 
            try {
                Organizer organizer = service.findByEmail(getEmail());
                
                // Access the original URL, if any
                if (requestedURI != null && !requestedURI.contains("/login")) {
                    getExternal().redirect(requestedURI);
                    return;
                }

                redirect("event/all");

            } catch (NotFoundException ex) {
                // VERY UNLIKE TO HAPPEN
            } catch (InternalServerErrorException | IOException ex) {
                redirectInternalServerError();
            }

        } catch (ServletException ex) {
            // FAILURE FAILURE FAILURE FAILURE FAILURE FAILURE FAILURE 
            error("Access Denied");
        } 
    }
    
    public void logout(ActionEvent event) {
        try {
            getRequest().logout();
            
            // REDIRECT TO LIST EVENTS
            redirect(hostEventService(), "");
            
        } catch(ServletException ex) {
            LOG.log(Level.SEVERE, "### logout() SERVLET EXCEPTION:{0}", 
                                                                ex.getMessage());
        }
    }
    
    @PostConstruct
    public void init() {
        // Get the original URL (if any)
        requestedURI = getOriginalURL();
    }
}
