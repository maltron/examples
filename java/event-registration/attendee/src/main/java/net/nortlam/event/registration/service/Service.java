package net.nortlam.event.registration.service;

import java.util.Collection;
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
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.jms.Messaging;
import net.nortlam.event.registration.util.AbstractService;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Stateless
public class Service extends AbstractService<Attendee> {
    
    private static final Logger LOG = Logger.getLogger(Service.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private Messaging messaging;

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
    }
    
    /**
     * Just save any Order from Event's Service */
    public void save(Order order) throws EntityExistsException, 
                        IllegalArgumentException, TransactionRequiredException {
        getEntityManager().persist(order);
    }
    
    /**
     * Delete an Order beause the attendee decided not to attend */
    public void delete(Order order) throws IllegalArgumentException, 
                                                TransactionRequiredException {
        getEntityManager().remove(getEntityManager()
                                            .find(Order.class, order.getID()));
    }
    
    
    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //  FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    
    public Collection<Order> listOrdersForAttendee(long attendeeID)
                                                    throws NoResultException,
                            NonUniqueResultException, QueryTimeoutException,
                            TransactionRequiredException, PessimisticLockException,
                            LockTimeoutException, PersistenceException {
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        
        query.select(root).where(builder.equal(
                root.get(Order.COLUMN_ATTENDEE), attendeeID))
                .orderBy(builder.asc(root.get(Order.COLUMN_STARTS)));
        
        return getEntityManager().createQuery(query).getResultList();        
    }
    
    public Attendee findByEmail(String email) throws NotFoundException, 
                                                    InternalServerErrorException {
        return findByProperty(em, Attendee.class, "email", email);
    }
    
    // MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 
    //  MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 

    public void notifyOrderRefund(Order order) throws JMSException {
        Connection connection = null; Session session = null;
        try {
            connection = messaging.connection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = messaging.topicOrderRefund();
            
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            LOG.log(Level.INFO, ">>> [ATTENDEE] notifyOrderRefund JSON:{0}", order.toString());
            TextMessage textMessage = session.createTextMessage(order.toString());
            producer.send(textMessage);
            
        } finally {
            if(session != null) try{session.close();}catch(JMSException ex) {}
            if(connection != null) try{connection.close();}catch(JMSException ex){}
        }
        
    }
    
    
    // ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    //  ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
