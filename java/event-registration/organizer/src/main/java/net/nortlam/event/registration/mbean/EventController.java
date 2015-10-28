package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

@ManagedBean(name="event")
@ViewScoped
public class EventController extends EventRegistrationCommonController 
                                                     implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(EventController.class.getName());
    
    @EJB
    private Service service;
    
    private Event event;
    private String eventID;
    private boolean isNew;
    private String ticketName;
    private int ticketQuantity;

    public EventController() {
    }
    
    public void setEventID(String eventID) {
        try {
            long value = Long.parseLong(eventID);
            event = service.requestEvent(hostEventService(), value);
            isNew = false;
            
        } catch(NumberFormatException ex) {
            LOG.log(Level.WARNING, "### setEventID() NUMBER FORMAT EXCEPTION: {0}",
                                                                ex.getMessage());
            redirectInternalServerError();
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getEventID() {return null;}// NOTHING TO DO
    
    public Event getEvent() {
        if(event == null) {
            event = new Event();
            event.setTickets(new HashSet<Ticket>());
            isNew = true;
        }
        
        return event;
    }
    
    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }
    
    public String getTicketName() {
        return ticketName;
    }
    
    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }
    
    public int getTicketQuantity() {
        return ticketQuantity;
    }
    
    public void addTicket(ActionEvent e) {
        Ticket newTicket = new Ticket(ticketName, ticketQuantity);
        event.getTickets().add(newTicket);
        ticketName = ""; ticketQuantity = 1;
    }
    
    public void removeTicket(Ticket ticket) {
        event.getTickets().remove(ticket);
    }
    
    public void save(ActionEvent e) {
        LOG.log(Level.INFO, ">>> EventController.save(): {0}", hostEventService());
        try {
            String hostname = hostEventService();
            if(isNew) event = service.requestCreateEvent(hostname, event);
            else event = service.requestUpdateEvent(hostname, event);
            
            isNew = false;
            
            info("Successfull", "Event's Information saved");
            
        } catch(AlreadyExistsException ex) {
            String message = String.format(
                            "Designation(%s) and Edition(%d) already exists",
                                    event.getDesignation(), event.getEdition());
            error("Already Exists", message);
        } catch(IllegalArgumentException ex) {
            error("Illegal Argument", ex.getMessage());
        } catch(MissingInformationException ex) {
            error("Missing Information", ex.getMessage());
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
}
