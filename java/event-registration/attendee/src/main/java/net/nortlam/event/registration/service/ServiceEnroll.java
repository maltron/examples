package net.nortlam.event.registration.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.nortlam.event.registration.entity.Enroll;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.util.AbstractService;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Stateless
public class ServiceEnroll extends AbstractService<Enroll> {

    private static final Logger LOG = Logger.getLogger(ServiceEnroll.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    public ServiceEnroll() {
        super(Enroll.class);
    }
    
    @Override
    public void validation(Enroll enroll, boolean isNew) 
            throws IllegalArgumentException, BiggerException, 
                            MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        if(enroll == null) {
            LOG.log(Level.WARNING, "### validation() Enroll instance is NULL");
            throw new IllegalArgumentException("### validation() Enroll instance is NULL");
        }
        
        if(enroll.getAttendeeID() == 0) {
            LOG.log(Level.WARNING, "### validation() AttendeeID is not set");
            throw new IllegalArgumentException("### validation() AttendeeID is not set");
        }
        
        if(enroll.getEventID() == 0) {
            LOG.log(Level.WARNING, "### validation() EventID is not set");
            throw new IllegalArgumentException("### validation() EventID is not set");
        }
        
        if(enroll.getTitle() != null && enroll.getTitle().length() > Event.LENGTH_TITLE) {
            LOG.log(Level.SEVERE, "### validation() Title's Length is"+
                    " bigger than {0} characters", Event.LENGTH_TITLE);
            throw new BiggerException(String.format("Title's length is"+
                    " bigger thand %d characters", Event.LENGTH_TITLE));
        }
    }
    
    // ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    //  ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
