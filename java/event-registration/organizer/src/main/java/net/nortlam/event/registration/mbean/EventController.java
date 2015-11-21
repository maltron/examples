package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.jms.JMSException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name="event")
@ViewScoped
public class EventController extends EventRegistrationCommonController 
                                                     implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(EventController.class.getName());
    
    public static enum ACTION {CANCEL}
    
    @EJB
    private Service service;
    
    private Event event;
    private String eventID;
    private boolean isNew;
    private String ticketName;
    private int ticketQuantity;
    
    // Logged Organizer
    private Organizer loggedOrganizer;
    
    // For Listing Events
    private Collection<Event> events;
    private Event eventSelected;
    
    // For Listing Attendees
    private Order orderSelected;

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
    
    public Organizer getLoggedOrganizer() {
        if(loggedOrganizer == null) {
            try {
                String email = getExternal().getRemoteUser();
                loggedOrganizer = service.findByEmail(email);
                
            } catch(NotFoundException ex) {
                redirectNotFoundError();
            } catch(InternalServerErrorException ex) {
                redirectInternalServerError();
            }
        }
        
        return loggedOrganizer;
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
        LOG.log(Level.INFO, ">>> EventController.save(): Remote User:", getExternal().getRemoteUser());
        
        try {
            String hostname = hostEventService();
            if(isNew) {
                Organizer organizer = service.findByEmail(getExternal().getRemoteUser());
                
                // IMPORTANT: Information on which Organizer is doing this event
                event.setOrganizer(organizer.getID());
                event = service.requestCreateEvent(hostname, event);
            } else event = service.requestUpdateEvent(hostname, event);
            
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
    
    // ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
    //  ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
    
    public void setOrderSelected(Order orderSelected) {
        this.orderSelected = orderSelected;
    }
    
    public Order getOrderSelected() {
        return orderSelected;
    }
    
    public void onOrderSelected(SelectEvent event) {
        LOG.log(Level.INFO, ">>> onOrderSelected()");
    }
    
    public ACTION[] getActions() {
        return ACTION.values();
    }
    
    public ACTION getActionSelected() {
        return null; // NOT USED
    }
    
    public void setActionSelected(ACTION action) {
        LOG.log(Level.INFO, ">>> setActionSelected({0}):", action);
        switch(action) {
            case CANCEL: 
                RequestContext.getCurrentInstance().execute(
                                        "PF('dialogCancelConfirm').show();");
                break;
        }
    }
    
    public void performActionCancel() {
        if(getOrderSelected() == null) {
            LOG.log(Level.SEVERE, "### performActionCancel() NO ORDER SELECTED");
            return;
        }
        
        try {
            service.notifyOrderRefund(getOrderSelected());
            redirect(String.format("event/%d/attendees", event.getID()));
        } catch(JMSException ex) {
            LOG.log(Level.SEVERE, "### JMS EXCEPTION:{0}", ex.getMessage());
            redirectInternalServerError();
        }
    }
    
    // LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS 
    //  LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS LIST EVENTS 
    
    public Collection<Event> getEvents() {
        if(events == null) {
            try {
                Organizer organizer = getLoggedOrganizer();
                events = service.requestEventsForOrganizer(hostEventService(), 
                                                            organizer.getID());
            } catch(NotFoundException ex) {
                redirectNotFoundError();
            } catch(InternalServerErrorException ex) {
                redirectInternalServerError();
            }
        }
        
        return events;
    }
    
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //   FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    
    public Collection<Order> listOrdersForEvent() 
                                        throws InternalServerErrorException {
        try {
            return service.listOrdersForEvent(getLoggedOrganizer(), getEvent().getID());
        } catch(IllegalStateException | QueryTimeoutException | 
                TransactionRequiredException | PessimisticLockException ex) {
            LOG.log(Level.SEVERE, "### ILLEGAL | QUERY TIMEOUT | "+
                    "TRANSACTION REQUIRED | PESSIMISTIC LOCK EXCEPTION "+
                    " {0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch(PersistenceException ex) {
            LOG.log(Level.SEVERE, "### PERSISTENCE EXCEPTION:{0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
//   throws IllegalStateException,
//                    QueryTimeoutException, TransactionRequiredException, 
//                                PessimisticLockException, LockTimeoutException, 
//                                                            PersistenceException { 
    
    
    public void setEventSelected(Event eventSelected) {
        this.eventSelected = eventSelected;
    }
    
    public Event getEventSelected() {
        return eventSelected;
    }
    
    public void onRowSelect(SelectEvent event) {
        String eventSelected = String.format("event/%d", 
                getEventSelected().getID());
        redirect(hostOrganizerService(), eventSelected);
    }
}
