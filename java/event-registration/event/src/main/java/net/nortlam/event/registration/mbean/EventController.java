package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

import org.primefaces.event.SelectEvent;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name="event")
@ViewScoped
public class EventController extends EventRegistrationCommonController 
                                                        implements Serializable {

    private static final Logger LOG = Logger.getLogger(EventController.class.getName());
    
    @EJB
    private Service service;
    
    private Event event = null;
    private String eventID;
    private boolean isNew;

    public EventController() {
    }
    
    public void setEventID(String eventID) {
        try {
            long value = Long.parseLong(eventID);
            event = service.findByID(value);
            isNew = false;
            
        } catch(NumberFormatException ex) {
            LOG.log(Level.WARNING, "### setEventID() NUMBER FORMAT EXCEPTION:{0}",
                                                                ex.getMessage());
            redirectInternalServerError();
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getEventID() { return null; } // NOTHING TO DO

    public Event getEvent() {
        if(event == null) {
            event = new Event();
            event.setTickets(new HashSet<Ticket>());
            isNew = true;
        }
        
        return event;
    }
    
    // LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    //  LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    public Collection<Event> listFutureEvents() {
        try {
            return service.listFutureEvents();
        } catch(NoResultException | NonUniqueResultException | QueryTimeoutException |
                TransactionRequiredException | PessimisticLockException | LockTimeoutException ex) {
            LOG.log(Level.SEVERE, "### listFutureEvents() <SEVERAL> Exception:{0}",
                    ex.getMessage());
        } catch(PersistenceException ex) {
            LOG.log(Level.SEVERE, "### listFutureEvents() PERSISTENCE EXCEPTION:{0}", ex.getMessage());
        }
        
        return null;
    }
    
    public void onRowSelect(SelectEvent event) {
        
    }
    
    // GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO 
    //  GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO GOTO 
    
    public String today() {
        return DATE_FORMAT.format(new Date());
    }
    
    public void goCreateEvent(ActionEvent event) {
        redirect(hostOrganizerService(), "/new");
    }
    
    public void goManageEvent(ActionEvent event) {
        redirect(hostOrganizerService(), "/manage");
    }
    
    
    
    // ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT 
    //  ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT ACTION EVENT 
    
    public void save(ActionEvent e) {
        try {
            if(isNew) event = service.create(event);
            else event = service.update(event);
            
            info("Successfull", "Event's Information saved");
            
        } catch(AlreadyExistsException ex) {
            // Event's Designation and Edition already exist
            error(ex.getMessage());
            
        } catch(BiggerException ex) {
            error(ex.getMessage());
            
        } catch(IllegalArgumentException ex) {
            error(ex.getMessage());
            
        } catch(MissingInformationException ex) {
            error(ex.getMessage());
            
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
}
