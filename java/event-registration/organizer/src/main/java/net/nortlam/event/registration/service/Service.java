package net.nortlam.event.registration.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.util.AbstractService;

@Stateless
public class Service extends AbstractService<Organizer> {
    
    private static final Logger LOG = Logger.getLogger(Service.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public void validation(Organizer organizer, boolean isNew) 
            throws IllegalArgumentException, BiggerException, 
                MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        if(organizer == null) throw new IllegalArgumentException();
        
        if(organizer.getEmail() == null) 
            throw new MissingInformationException("Email is Missing");
        
        if(organizer.getEmail().length() > Organizer.LENGTH_EMAIL)
            throw new BiggerException(String.format(
                    "Email is bigger than %d characters", Organizer.LENGTH_EMAIL));
        
        if(organizer.getFirstName() == null)
            throw new MissingInformationException("First Name is Missing");
        
        if(organizer.getFirstName().length() > Organizer.LENGTH_FIRSTNAME)
            throw new BiggerException(String.format(
                    "First Name is bigger than %d characters", Organizer.LENGTH_FIRSTNAME));

        if(organizer.getLastName() == null)
            throw new MissingInformationException("Last Name is Missing");

        if(organizer.getLastName().length() > Organizer.LENGTH_LASTNAME)
            throw new BiggerException(String.format(
                    "Last Name is bigger than %d characters", Organizer.LENGTH_LASTNAME));
        
        if(organizer.getPassword() == null)
            throw new MissingInformationException("Password is Missing");
        
        if(organizer.getPassword().length() > Organizer.LENGTH_PASSWORD)
            throw new BiggerException(String.format(
                    "Password is bigger than %d characters", Organizer.LENGTH_EMAIL));
    }
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //  FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    public Organizer findByEmail(String email) throws NotFoundException, 
                                                    InternalServerErrorException {
        return findByProperty(em, Organizer.class, "email", email);
    }
    
    public Organizer findByFirstLastName(String firstName, String lastName)
                        throws NotFoundException, InternalServerErrorException {
        return findByProperty(em, Organizer.class, "firstName", firstName,
                                                            "lastName", lastName);
    }
}
