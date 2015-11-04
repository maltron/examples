package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

import org.primefaces.event.SelectEvent;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Named("event")
@ViewScoped
public class EventController extends EventRegistrationCommonController 
                                                        implements Serializable {

    private static final Logger LOG = Logger.getLogger(EventController.class.getName());
    
    @EJB
    private Service service;
    
    // Used basic for a list of Events
    private Event eventSelected;

    public EventController() {
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
    
    public void setEventSelected(Event eventSelected) {
        this.eventSelected = eventSelected;
    }
    
    public Event getEventSelected() {
        return eventSelected;
    }
    
    public void onRowSelect(SelectEvent event) {
        String eventSelected = String.format("%s%d", 
                getEventSelected().getDesignation(),
                getEventSelected().getEdition());
        redirect(hostEventService(), eventSelected);
    }
}
