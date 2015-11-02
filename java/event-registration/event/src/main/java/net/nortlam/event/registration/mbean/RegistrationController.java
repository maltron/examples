package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;
import static net.nortlam.event.registration.util.Extraction.extractDesignation;
import static net.nortlam.event.registration.util.Extraction.extractEdition;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name="register")
@RequestScoped
public class RegistrationController extends EventRegistrationCommonController
                                                        implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(RegistrationController.class.getName());
    
    private static final int[] DEFAULT_QUANTITY_SELECTION = 
                                    new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    @EJB
    private Service service;
    
    private String eventID;
    private Event event;
    private Attendee attendee;

    public RegistrationController() {
    }
    
    public void setDesignationEdition(String designationEdition) {
        String designation = extractDesignation(designationEdition);
        int edition = extractEdition(designationEdition);
        
        if(designation == null || edition == 0) {
            LOG.log(Level.WARNING, "### setDesignationEdition() Arguments *NOT* valid:"+
                    " Designation:{0} Edition:{1}", new Object[] {designation, edition});
            redirectNotFoundError();
            return;
        }
        
        try {
            event = service.findByDesignationEdition(designation, edition);
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getDesignationEdition() { return null; } // NOT USED
    
    public Event getEvent() {
        if(event == null) LOG.log(Level.SEVERE, "### getEvent() #1 event is NULL");
        return event; 
    }
    
    public int[] getDefaultTicketQuantity() {
        return DEFAULT_QUANTITY_SELECTION;
    }
    
    public Organizer getOrganizer() {
        if(event == null) LOG.log(Level.SEVERE, "### getEvent() #2 event is NULL");
        try {
            if(event.getOrganizer() > 0)
                return service.requestOrganizerByID(hostOrganizerService(), 
                                                        event.getOrganizer());
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
        
        return null;
    }
    
    public Attendee getAttendee() {
        if(attendee == null) {
            attendee = new Attendee();
        }
        
        return attendee;
    }
    
    private boolean isTicketSelected() {
        if(event == null) {
            LOG.log(Level.WARNING, "### isTicketSelected() NO EVENT IS SELECTED");
        }
        
        boolean found = false;
        for(Ticket ticket: event.getTickets()) {
            LOG.log(Level.INFO, ">>> isTicketSelected() {0}", ticket.toString());
            if(ticket.getQuantitySelected() > 0) {
                found = true; break;
            }
        }
        
        return found;
    }
    
    public String performRegister() {
        // At least, one ticket must be selected in order to perform a sell
        if(!isTicketSelected()) {
            error("You need to select a quantity in order to register");
            return null;
        }
        
        
        return "/event_registration.xhtml";
    }
    
    public String register() {
        // At least, one ticket must be selected in order to perform a sell
        if(!isTicketSelected()) {
            error("You need to select a quantity in order to register");
            return null;
        }
        
        return "ticket2";
    }
}
