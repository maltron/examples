package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;
import org.primefaces.event.SelectEvent;

@Named("attendee")
@ViewScoped
public class AttendeeController extends EventRegistrationCommonController 
                                                    implements Serializable {

    private static final Logger LOG = Logger.getLogger(AttendeeController.class.getName());
    @EJB
    private Service service;
    
    private boolean isNew;
    private Attendee attendee;
    
    // Logged Attendee
    private Attendee loggedAttendee;
    
    private Order orderSelected;
    

    public AttendeeController() {}
    
    public void setAttendeeID(String attendeeID) {
        try {
            long value = Long.parseLong(attendeeID);
            attendee = service.read(value);
            isNew = false;
            
        } catch(NumberFormatException ex) {
            LOG.log(Level.WARNING, "### setAttendeID() NUMBER FORMAT EXCEPTION:",
                                                                ex.getMessage());
            redirectInternalServerError();
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getAttendeeID() {return null;} // NOTHING TO DO
    
    public Attendee getAttendee() {
        if(attendee == null) {
            isNew = true;
            attendee = new Attendee();
        }
        
        return attendee;
    }
    
    public Attendee getLoggedAttendee() {
        if(loggedAttendee == null) {
            try {
                String email = getExternal().getRemoteUser();
                loggedAttendee = service.findByEmail(email);
                
            } catch(NotFoundException ex) {
                redirectNotFoundError();
            } catch(InternalServerErrorException ex) {
                redirectInternalServerError();
            }
        }
        
        return loggedAttendee;
    }
    
    // LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    //  LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    
    public Collection<Order> listOrdersForAttendee() {
        try {
            return service.listOrdersForAttendee(getLoggedAttendee().getID());
        } catch(NoResultException | NonUniqueResultException | QueryTimeoutException |
                TransactionRequiredException | PessimisticLockException | LockTimeoutException ex) {
            LOG.log(Level.SEVERE, "### listEventsForAttendee() <SEVERAL> Exception:{0}",
                    ex.getMessage());
        } catch(PersistenceException ex) {
            LOG.log(Level.SEVERE, "### listEventsForAttendee() PERSISTENCE EXCEPTION:{0}", ex.getMessage());
        }
        
        return null;
    }

    public Order getOrderSelected() {
        return orderSelected;
    }

    public void setOrderSelected(Order enrollSelected) {
        this.orderSelected = enrollSelected;
    }
    
    public void onRowSelect(SelectEvent event) {
//        String eventSelected = String.format("event/%d", 
//                getEventSelected().getID());
//        redirect(hostOrganizerService(), eventSelected);
    }
    
    // PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT 
    //  PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT PERSITENT 
    public void save(ActionEvent event) {
        LOG.log(Level.INFO, ">>> save() isNew ? {0} Attendee ID:{1}", new Object[] {
            isNew, attendee.getID()
        });
        try {
            if(isNew) attendee = service.create(attendee);
            else attendee = service.update(attendee);
            
            isNew = false;
            info("Successfull", "Attendee information saved");
            
        } catch(AlreadyExistsException ex) {
            error("Already Exists", ex.getMessage());
        } catch(BiggerException | MissingInformationException ex) {
            error("Illegal Information", ex.getMessage());
        } catch(IllegalArgumentException ex) {
            error("Illegal Argument", ex.getMessage());
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
}
