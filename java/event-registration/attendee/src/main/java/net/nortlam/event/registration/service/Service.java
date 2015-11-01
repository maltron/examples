package net.nortlam.event.registration.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.util.AbstractService;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Stateless
public class Service extends AbstractService<Attendee> {
    
    private static final Logger LOG = Logger.getLogger(Service.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    public Service() {
        super(Attendee.class);
    }
    

    @Override
    public void validation(Attendee attendee, boolean isNew) 
            throws IllegalArgumentException, BiggerException, 
                            MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        
        if(attendee == null) {
            LOG.log(Level.WARNING, "### validation() Attendee instance is NULL");
            throw new IllegalArgumentException("### validation() Attendee instance is NULL");
        }
        
        if(attendee.getFirstName() == null) {
            LOG.log(Level.SEVERE, "### validation() First Name is NULL");
            throw new MissingInformationException("First Name is NULL");
        }
        
        if(attendee.getFirstName().length() > Attendee.LENGTH_FIRST_NAME) {
            LOG.log(Level.SEVERE, "### validation() First Name's Length is"+
                    " bigger than {0} characters", Attendee.LENGTH_FIRST_NAME);
            throw new BiggerException(String.format("First Name's length is"+
                    " bigger thand %d characters", Attendee.LENGTH_FIRST_NAME));
        }
        
        if(attendee.getLastName() == null) {
            LOG.log(Level.SEVERE, "### validation() Last Name is NULL");
            throw new MissingInformationException("Last Name is NULL");
        }
        
        if(attendee.getLastName().length() > Attendee.LENGTH_LAST_NAME) {
            LOG.log(Level.SEVERE, "### validation() Last Name's Length is"+
                    " bigger than {0} characters", Attendee.LENGTH_LAST_NAME);
            throw new BiggerException(String.format("Last Name's length is"+
                    " bigger thand %d characters", Attendee.LENGTH_LAST_NAME));
        }

        if(attendee.getEmail() == null) {
            LOG.log(Level.SEVERE, "### validation() Email is NULL");
            throw new MissingInformationException("Email is NULL");
        }
        
        if(attendee.getEmail().length() > Attendee.LENGTH_EMAIL) {
            LOG.log(Level.SEVERE, "### validation() Email's Length is"+
                    " bigger than {0} characters", Attendee.LENGTH_EMAIL);
            throw new BiggerException(String.format("Email's length is"+
                    " bigger thand %d characters", Attendee.LENGTH_EMAIL));
        }
        
        // EMAIL CHECK
        try {
            Attendee found = findByEmail(attendee.getEmail());
            if(isNew) throw new AlreadyExistsException(
                String.format("Email %s already exists", attendee.getEmail()));
            else if(found.getID() != attendee.getID())
                throw new AlreadyExistsException(
                String.format("Email %s already exists", attendee.getEmail()));
            
        } catch(NotFoundException ex) {
            // NOTHING TO DO
        }
        
        // FIRST AND LAST NAME
        try {
            Attendee found = findByFirstLastName(attendee.getFirstName(), 
                                                        attendee.getLastName());
            if(isNew) throw new AlreadyExistsException(
                String.format("First (%s) and Last(%s) already exists",
                        attendee.getFirstName(), attendee.getLastName()));
            else if(found.getID() != attendee.getID())
                throw new AlreadyExistsException(
                String.format("First (%s) and Last(%s) already exists",
                        attendee.getFirstName(), attendee.getLastName()));
            
        } catch(NotFoundException ex) {
            // NOTHING TO DO
        }
        
    }
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //  FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    
    public Attendee findByEmail(String email) throws NotFoundException, 
                                                    InternalServerErrorException {
        return findByProperty(em, Attendee.class, "email", email);
    }
    
    public Attendee findByFirstLastName(String firstName, String lastName)
                        throws NotFoundException, InternalServerErrorException {
        return findByProperty(em, Attendee.class, "firstName", firstName,
                                                            "lastName", lastName);
    }
    

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
