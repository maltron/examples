package net.nortlam.event.registration.service;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import net.nortlam.event.registration.entity.Event;
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

    public Service() {
        super(Organizer.class);
    }

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
        
        // EMAIL CHECK
        try {
            Organizer found = findByEmail(organizer);
            if(isNew) throw new AlreadyExistsException(
                String.format("Email %s already exists", organizer.getEmail()));
            else if(found.getID() != organizer.getID())
                throw new AlreadyExistsException(
                String.format("Email %s already exists", organizer.getEmail()));
            
        } catch(NotFoundException ex) {
            // NOTHING TO DO
        }
        
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

        // FIRST NAME AND LAST NAME CHECK
        try {
            Organizer found = findByFirstLastName(organizer);
            if(isNew) throw new AlreadyExistsException(
                String.format("First and Lastname (%s %s) already exists", organizer.getFirstName(),
                        organizer.getLastName()));
            else if(found.getID() != organizer.getID())
                throw new AlreadyExistsException(
                String.format("First and Lastname (%s %s) already exists", organizer.getFirstName(),
                        organizer.getLastName()));
            
        } catch(NotFoundException ex) {
            // NOTHING TO DO
        }
        
        if(organizer.getPassword() == null)
            throw new MissingInformationException("Password is Missing");
        
        if(organizer.getPassword().length() > Organizer.LENGTH_PASSWORD)
            throw new BiggerException(String.format(
                    "Password is bigger than %d characters", Organizer.LENGTH_EMAIL));
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //  FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    public Organizer findByEmail(Organizer organizer) throws NotFoundException, 
                                                    InternalServerErrorException {
        return findByProperty(em, Organizer.class, "email", organizer.getEmail());
    }
    
    public Organizer findByFirstLastName(Organizer organizer)
                        throws NotFoundException, InternalServerErrorException {
        return findByProperty(em, Organizer.class, "firstName", organizer.getFirstName(),
                                           "lastName", organizer.getLastName());
    }
    
    // REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST 
    //  REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST
    public Event requestEvent(String host, long ID) throws NotFoundException,
                                                 InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api/{ID}").build(ID);
        return request(uri, Event.class);
    }

//    public Response create(Event event) throws IllegalArgumentException, 
//            BiggerException, MissingInformationException, AlreadyExistsException, 
//                                                InternalServerErrorException {
    
    public Event requestCreateEvent(String host, Event event) 
                            throws AlreadyExistsException, IllegalArgumentException,
                                MissingInformationException, NotFoundException, 
                                                    InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api").build();
        Event inserted = null;
        
        Response response = null;
        try {
            response = post(uri, event);
            if(response.getStatus() == Response.Status.CREATED.getStatusCode())
                inserted = response.readEntity(Event.class);
            else if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new NotFoundException();
            else if(response.getStatus() == Response.Status.CONFLICT.getStatusCode())
                throw new AlreadyExistsException();
            else if(response.getStatus() == Response.Status.EXPECTATION_FAILED.getStatusCode())
                throw new IllegalArgumentException();
            else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode())
                throw new MissingInformationException();
            
            else {
                Response.StatusType info = response.getStatusInfo();
                LOG.log(Level.SEVERE, "### requestCreateEvent() PROBLEM:{0} {1}",
                        new Object[] {response.getStatus(),
                            info != null ? info.getReasonPhrase() : "<NO REASON GIVEN>"});
                throw new InternalServerErrorException();
            }
        } finally {
            if(response != null) response.close();
        }
        
        return inserted;
    }
    
    public Event requestUpdateEvent(String host, Event event)
                        throws NotFoundException, InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api").build();
        Event updated = null;
        
        Response response = null;
        try {
            response = put(uri, event);
            if(response.getStatus() == Response.Status.ACCEPTED.getStatusCode())
                updated = response.readEntity(Event.class);
            else if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new NotFoundException();
            else {
                Response.StatusType info = response.getStatusInfo();
                LOG.log(Level.SEVERE, "### requestUpdateEvent() PROBLEM:{0} {1}",
                        new Object[] {response.getStatus(),
                            info != null ? info.getReasonPhrase() : "<NO REASON GIVEN>"});
            }
        
        } finally {
            if(response != null) response.close();
        }
        
        return updated;
    }
    
    public boolean requestDeleteEvent(String host, Event event) 
                        throws NotFoundException, InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api/{ID}").build(event.getID());
        
        boolean success = false;
        Response response = null;
        try {
            response = delete(uri, event);
            if(response.getStatus() == Response.Status.OK.getStatusCode())
                success = true;
            else if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new NotFoundException();
            else {
                Response.StatusType info = response.getStatusInfo();
                LOG.log(Level.SEVERE, "### requestDeleteEvent() PROBLEM:{0} {1}",
                        new Object[] {response.getStatus(),
                            info != null ? info.getReasonPhrase() : "<NO REASON GIVEN>"});
            }
        } finally {
            if(response != null) response.close();
        }
        
        return success;
    }
}