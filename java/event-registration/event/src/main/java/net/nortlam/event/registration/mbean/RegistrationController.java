package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
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
@Named("register")
@ViewScoped
public class RegistrationController extends EventRegistrationCommonController
                                                        implements Serializable {
    private static final int[] VALUES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Event event;
    private Organizer organizer;
    private Attendee attendee;
    
    @EJB
    private Service service;
    
    private static final Logger LOG = Logger.getLogger(RegistrationController.class.getName());

    public RegistrationController() {
    }

    public int[] getDefaultQuantity() {
        return VALUES;
    }
    
    public void setDesignationEdition(String designationEdition) {
        LOG.log(Level.INFO, ">>> setDesignationEdition({0})", designationEdition);
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
        return event;
    }
    
    public Organizer getOrganizer() {
        if(organizer == null) {
            try {
                if(event.getOrganizer() > 0) {
                    organizer = service.requestOrganizerByID(hostOrganizerService(), 
                                                        event.getOrganizer());
                } else {
                    LOG.log(Level.SEVERE, "### getOrganizer() UNABLE TO FETCH ORGANIZER DATA");
                }
            } catch(NotFoundException ex) {
                redirectNotFoundError();
            } catch(InternalServerErrorException ex) {
                redirectInternalServerError();
            }
        }
        
        return organizer;
    }
    
    public Attendee getAttendee() {
        if(attendee == null) {
            attendee = new Attendee();
        }
        
        return attendee;
    }
    
    public Set<Ticket> getTicketOrdered() {
        Set<Ticket> ordered = new HashSet<Ticket>();
        for(Ticket ticket: event.getTickets())
            if(ticket.getQuantitySelected() > 0)
                ordered.add(ticket);
        
        return ordered;
    }
    
    private boolean isTicketOrdered() {
        boolean found = false;
        for(Ticket ticket: event.getTickets())
            if(ticket.getQuantitySelected() > 0) {
                found = true; break;
            }
        
        return found;
    }
    
    @PostConstruct
    private void init() {
        LOG.log(Level.INFO, ">>> init() Fetching Event");
        event = (Event)getFlash().get(KEY_EVENT);
        LOG.log(Level.INFO, ">>> init() GET {0}", event);
    }
    
    public void registrationActionListener(ActionEvent e) {
        LOG.log(Level.INFO, ">>> registrationActionListener()");
        // First, check if there is any ticket selected.
        // It must be greater then ZERO
        if(!isTicketOrdered()) {
            error("No tickets selected", 
                    "You must select a certain quantity of tickets first");
            throw new AbortProcessingException("### registrationActionListener() "+
                    " No tickets were selected");
        }
        
        Event temp = (Event)getFlash().put(KEY_EVENT, event);
        LOG.log(Level.INFO, ">>> registrationActionListener() PUT {0}", temp);
    }
    
    public void registration() {
        LOG.log(Level.INFO, ">>> registration()");
        //return "event_registration?faces-redirect=true";
        redirect("registration/new");
    }
    
    public void register() {
        LOG.log(Level.INFO, ">>> register()");
        
        // Registration Pre-Verification
        
        // Step #1: Verify if this Email already exists
        //          YES: OK, move forward for the registration
        //           NO: Redirect to Attendee Service for Account creation
        //               Using the email, First and Last Name already typed
        try {
            attendee = service.requestAttendeeByEMail(hostAttendeeService(), attendee.getEmail());
            // Yes, it does exist
        } catch(NotFoundException ex) {
            error("Account doesn't exist", "Please create an Attendee account before purchasing your tickets");
            return;
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
        
        // Step #2: Verify if there is enough tickets available to sell to
        ///         YES: Ok, move forward 
        //           NO: Error Message 
        
        
        // Step #3: Purchase it and notify everyon interested in the purchase process
        try {
            service.purchase(event, attendee);
            
        } catch(EntityExistsException | IllegalArgumentException | 
                            TransactionRequiredException ex) {
            LOG.log(Level.SEVERE, "### register() EXCEPTION:{0}", ex.getMessage());
            redirectInternalServerError();
        }
        
        
        redirect("registration/success");
    }
    
}
