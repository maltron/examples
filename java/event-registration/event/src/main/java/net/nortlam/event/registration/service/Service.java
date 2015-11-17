package net.nortlam.event.registration.service;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityExistsException;
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
import javax.ws.rs.core.UriBuilder;
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.jms.Messaging;
import net.nortlam.event.registration.util.AbstractService;

@Stateless
public class Service extends AbstractService<Event> {

    private static final Logger LOG = Logger.getLogger(Service.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private Messaging messaging;
    
    public Service() {
        super(Event.class);
    }

    @Override
    public Event create(Event event) throws IllegalArgumentException, 
                                    BiggerException, MissingInformationException, 
                            AlreadyExistsException, InternalServerErrorException {
        // It's important during first creation, the remaining tickets
        // must be updated accordigly 
        long total = 0;
        for(Ticket ticket: event.getTickets())
            total += ticket.getQuantityAvailable();
        
        event.setRemainingTickets(total);
        
        return super.create(event); 
    }
    
    

    @Override
    public void validation(Event event, boolean isNew) 
            throws IllegalArgumentException, BiggerException, 
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        if(event == null) {
            LOG.log(Level.WARNING, "### validation() Event argument is Missing");
            throw new IllegalArgumentException("Event argument is Missing");
        }
        
        // ID
        // edition (it cannot be zero)
        if(event.getEdition() <= 0) {
            LOG.log(Level.WARNING, "### validation() Event's Edition cannot be Zero (or negative)");
            throw new IllegalArgumentException(
                                "Event's Edition cannot be Zero (or negative)");
        }
        
        // designation
        if(event.getDesignation() == null) {
            LOG.log(Level.WARNING, "### validation() Designation is Missing");
            throw new MissingInformationException("Designation is Missing");
        }
        
        if(event.getDesignation().length() > Event.LENGTH_DESIGNATION) {
            LOG.log(Level.WARNING, "### validation() Designation is bigger "+
                            "than {0} characters", Event.LENGTH_DESIGNATION);
            throw new BiggerException(String.format(
                                    "Designation is bigger than %d characters",
                                                    Event.LENGTH_DESIGNATION));
        }
        
        try {
            Event found = findByDesignationEdition(event);
            String messageError = String.format(
                    "Event's Designation (%s) and Edition (%d) already exists",
                    event.getDesignation(), event.getEdition());
            
            if(isNew) throw new AlreadyExistsException(messageError);
            else if(found.getID() != event.getID())
                throw new AlreadyExistsException(messageError);
            
        } catch(NotFoundException ex) {
            // NOTHING TO DO.
        }
        
        // title
        if(event.getTitle() == null) {
            LOG.log(Level.WARNING, "### validation() Title is Missing");
            throw new MissingInformationException("Title is Missing");
        }
        
        if(event.getTitle().length() > Event.LENGTH_TITLE) {
            LOG.log(Level.WARNING, "### validation() Title is bigger than "+
                    "{0} characters", Event.LENGTH_TITLE);
            throw new BiggerException(String.format(
                                    "Title is bigger than %d characters",
                                                    Event.LENGTH_TITLE));
        }
        // location
        if(event.getLocation() != null && 
                            event.getLocation().length() > Event.LENGTH_LOCATION) {
            LOG.log(Level.WARNING, "### validation() Location is bigger than"+
                    " {0} characters", Event.LENGTH_LOCATION);
            throw new BiggerException(String.format(
                                    "Location is bigger than %d characters",
                                                    Event.LENGTH_LOCATION));
        }
        
        // address
        if(event.getAddress() != null &&
                event.getAddress().length() > Event.LENGTH_ADDRESS) {
            LOG.log(Level.WARNING, "### validation() Address is bigger than"+
                    " {0} characters", Event.LENGTH_ADDRESS);
            throw new BiggerException(String.format(
                                    "Address is bigger than %d characters",
                                                    Event.LENGTH_ADDRESS));
        }
        // city
        if(event.getCity() != null &&
                event.getCity().length() > Event.LENGTH_CITY) {
            LOG.log(Level.WARNING, "### validation() City is bigger than"+
                    " {0} characters", Event.LENGTH_CITY);
            throw new BiggerException(String.format(
                                    "City is bigger than %d characters",
                                                            Event.LENGTH_CITY));
        }
        
        // region
        if(event.getRegion() != null &&
                event.getRegion().length() > Event.LENGTH_REGION) {
            LOG.log(Level.WARNING, "### validation() Region is bigger than"+
                    " {0} characters", Event.LENGTH_REGION);
            throw new BiggerException(String.format(
                                    "Region is bigger than %d characters",
                                                    Event.LENGTH_REGION));
        }
        
        // zipCode
        if(event.getZipCode() != null && event.getZipCode().length() > Event.LENGTH_ZIP_CODE) {
            LOG.log(Level.WARNING, "### validation() ZIPCode is bigger than"+
                    " {0} characters", Event.LENGTH_ZIP_CODE);
            throw new BiggerException(String.format(
                                    "ZIPCode is bigger than %d characters",
                                                    Event.LENGTH_ZIP_CODE));
        }
        
       // country
        if(event.getCountry() != null && 
                            event.getCountry().length() > Event.LENGTH_COUNTRY) {
            LOG.log(Level.WARNING, "### validation() Country is bigger than"+
                    " {0} characters", Event.LENGTH_DESIGNATION);
            throw new BiggerException(String.format(
                                    "Country is bigger than %d characters",
                                                    Event.LENGTH_DESIGNATION));
        }
        
        // starts
        if(event.getStarts() == null) {
            LOG.log(Level.WARNING, "### validation() Event's Start date is Missing");
            throw new MissingInformationException("Event's Start date is Missing");
        }
        
        // ends
        if(event.getEnds() == null) {
            LOG.log(Level.WARNING, "### validation() Event's End date is Missing");
            throw new MissingInformationException("Event's End date is Missing");
        }
        
        // ends must be bigger than starts
        if(!event.getEnds().after(event.getStarts())) {
            LOG.log(Level.WARNING, "### validation() Event's date ends is after"+
                    " Event's date starts");
            throw new IllegalArgumentException(
                                "Event's date ends is after Event's date starts");
        }
        
        // description
        if(event.getDescription() != null && 
                    event.getDescription().length() > Event.LENGTH_DESCRIPTION) {
            LOG.log(Level.WARNING, "### validation() Description is bigger "+
                    "than {0} characters", Event.LENGTH_DESCRIPTION);
            throw new BiggerException(String.format(
                                    "Description is bigger than %d characters",
                                                    Event.LENGTH_DESCRIPTION));
        }
        
        // Organizer
        if(event.getOrganizer() == 0) {
            LOG.log(Level.WARNING, "### validation() Organizer's is Missing");
            throw new MissingInformationException("Organizer's is Missing");
        }
        
        
        // Tickets
        if(event.getTickets() == null) {
            LOG.log(Level.WARNING, "### validation() Ticket's is Missing");
            throw new MissingInformationException("Ticket's is Missing");
        }
        
        // Remaining Tickets
        if(event.getRemainingTickets() > 0) {
            LOG.log(Level.WARNING, "### validation() Remaining Tickets must be greater than zero");
            throw new MissingInformationException("Remaining Tickets must be great than zero");
        }
    }
    
    // LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    //  LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST LIST 
    public Collection<Event> listFutureEvents() throws NoResultException,
                            NonUniqueResultException, QueryTimeoutException,
                            TransactionRequiredException, PessimisticLockException,
                            LockTimeoutException, PersistenceException {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        
        query.select(root).where(builder.greaterThan(
                root.<Date>get(Event.COLUMN_EVENT_STARTS), new Date()))
                .orderBy(builder.asc(root.get(Event.COLUMN_EVENT_STARTS)));

        return getEntityManager().createQuery(query).getResultList();
    }
    
    public Collection<Event> listEventsForOrganizer(long organizerID) 
                                                    throws NoResultException,
                            NonUniqueResultException, QueryTimeoutException,
                            TransactionRequiredException, PessimisticLockException,
                            LockTimeoutException, PersistenceException {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        
        query.select(root).where(builder.equal(
                root.get(Event.COLUMN_ORGANIZER), organizerID))
                .orderBy(builder.asc(root.get(Event.COLUMN_EVENT_STARTS)));
        
        return getEntityManager().createQuery(query).getResultList();
    }
    
    public boolean alreadyExistOrderFor(Event event, Attendee attendee) 
                                            throws InternalServerErrorException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        
        query.select(root).where(builder.and(
                builder.equal(root.get(Order.COLUMN_EVENT), event.getID()),
                builder.equal(root.get(Order.COLUMN_ATTENDEE), attendee.getID())));
        
        boolean found = false;
        try {
            getEntityManager().createQuery(query).getSingleResult();
            found = true;
        } catch(NoResultException ex) {
            // There isn't an order for this Attendee 
        } catch(NonUniqueResultException | QueryTimeoutException | 
                TransactionRequiredException | PessimisticLockException |
                LockTimeoutException ex) {
            LOG.log(Level.SEVERE, "### NON UNIQUE RESULT | QUERY TIMEOUT | "+
                    "TRANSACTION REQUIRED | PESSIMISTIC LOCK EXCEPTION EXCEPTION"+
                    " {0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
            
        } catch(PersistenceException ex) {
            LOG.log(Level.SEVERE, "### PERSISTENCE EXCEPTION EX:{0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
        
        return found;
    }
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //  FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    
    public Event findByDesignationEdition(String designation, int edition)
                        throws NotFoundException, InternalServerErrorException {
        return findByProperty(em, Event.class, "designation", designation,
                                    "edition", edition);
    }
    
    public Event findByDesignationEdition(Event event) throws NotFoundException,
                                                    InternalServerErrorException {
        return findByProperty(em, Event.class, "designation", 
                        event.getDesignation(), "edition", event.getEdition());
    }
    
    public Event findByID(long ID) throws NotFoundException, InternalServerErrorException {
        return findByProperty(em, Event.class, "ID", ID);
    }
    
    // TRANSACTION TRANSACTION TRANSACTION TRANSACTION TRANSACTION TRANSACTION 
    //   TRANSACTION TRANSACTION TRANSACTION TRANSACTION TRANSACTION TRANSACTION 

    /**
     * PENDING: Ideally, this kind of transaction shouldn't be done in this way.
     *          A Transaction Manager must be create in order to safely handle
     *          this kind of transaction */
    public boolean purchase(Event event, Attendee attendee) 
            throws IllegalArgumentException, EntityExistsException, 
                                                TransactionRequiredException {
        boolean success = false;
        
        // Step #1: Create a Order
        Order newOrder = new Order(event, attendee);
        
        // Step #2: Save it a Order
        em.persist(newOrder);
        
        // Step #4: Notify Everyone that a new Order was placed
        //          1 - Attendee: So it can list of a events enrolled
        //          2 - Event: So it can adjust the number of remaining tickets
        //          3 - Organizer: So it can track 
        try {
            notifyOrder(newOrder);
        } catch(JMSException ex) {
            LOG.log(Level.SEVERE, "### JMS EXCEPTION:{0}", ex.getMessage());
        } 
        
        return success;
    }
    
    // MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 
    //   MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 
    protected void notifyOrder(Order order) throws JMSException {
        Connection connection = null; Session session = null;
        try {
            connection = messaging.connection(); connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = messaging.topicOrder();
            
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            TextMessage textMessage = session.createTextMessage(order.toString());
            producer.send(textMessage);
            
        } finally {
            if(session != null) try{session.close();}catch(JMSException e){}
            if(connection != null) try{connection.close();}catch(JMSException e){}
        }
    }
    
    
    // REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST 
    //  REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST 
    
    public Organizer requestOrganizerByID(String host, long ID) 
                        throws NotFoundException, InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api/v1/{ID}").build(ID);
        return request(uri, Organizer.class);
    }
    
    public Attendee requestAttendeeByEMail(String host, String email) 
                        throws NotFoundException, InternalServerErrorException {
        URI uri = UriBuilder.fromUri(host).path("/api/v1/email/{email}").build(email);
        return request(uri, Attendee.class);
    }
    
//    public Enroll postEnrollAttendeeToEvent(String host, Event event, Attendee attendee)
//                        throws NotFoundException, InternalServerErrorException {
//        URI uri = UriBuilder.fromUri(host).path("/api/v1/enroll").build();
//        
//        Enroll newEnroll = new Enroll(event, attendee);
//        
//        Response response = null; Enroll enroll = null;
//        try {
//            response = post(uri, newEnroll);
//            if(response.getStatus() == Response.Status.CREATED.getStatusCode())
//                enroll = response.readEntity(Enroll.class);
//            // ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR 
//            else if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
//                throw new NotFoundException();
//            } else {
//                Response.StatusType info = response.getStatusInfo();
//                LOG.log(Level.SEVERE, "### request() PROBLEM:{0} {1}",
//                        new Object[] {response.getStatus(),
//                            info != null ? info.getReasonPhrase() : "<NO REASON GIVEN>"});
//            }
//            
//        } finally {
//            if(response != null) response.close();
//        }
//        
//        return enroll;
//    }
    
    // ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER
    //  ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
