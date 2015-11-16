package net.nortlam.event.registration.util;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.jms.Messaging;

public abstract class AbstractService<T> {
    
    private Class<T> className;
    
    @Inject
    private Messaging messaging;
    
    public AbstractService(Class<T> className) {
        this.className = className;
    }
            
    
    public T create(T t) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        validation(t, true);
        
        getEntityManager().persist(t);
        return t;
    }
    
    public T read(Object ID) throws NotFoundException, 
                                                 InternalServerErrorException {
        try {
            T found = getEntityManager().find(className, ID);
            if(found == null) throw new NotFoundException();
            
            return found;
        } catch(IllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "### read() ILLEGAL ARGUMENT EXCEPTION:{0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
    public T update(T t) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        validation(t, false);
        
        return getEntityManager().merge(t);
    }
    
    public void delete(Object ID) 
                        throws NotFoundException, InternalServerErrorException {
        try {
            T found = getEntityManager().find(className, ID);
            getEntityManager().remove(found);
            
        } catch(IllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "### read() ILLEGAL ARGUMENT EXCEPTION:{0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch(TransactionRequiredException ex) {
            LOG.log(Level.SEVERE, "### read() TRANSACTION REQUIRED EXCEPTION:{0}",
                    ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }
    
    public abstract void validation(T t, boolean isNew) throws IllegalArgumentException,
            BiggerException, MissingInformationException, AlreadyExistsException, 
                                                    InternalServerErrorException;
    
    public abstract EntityManager getEntityManager();
    
    private static final Logger LOG = Logger.getLogger(AbstractService.class.getName());

    // FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    //   FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER FINDER 
    
    public T findByProperty(EntityManager em, Class<T> className, Object ... propertyValue) 
                            throws NotFoundException, InternalServerErrorException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(className);
        Root<T> root = query.from(className);
        
        Predicate[] predicate = new Predicate[propertyValue.length/2]; int count = 0;
        for(int i=0; i < propertyValue.length; i += 2) 
            predicate[count++] = builder.equal(root.get(propertyValue[i].toString()), 
                                                            propertyValue[i+1]);
        query.select(root).where(builder.and(predicate));
        
        try {
            return em.createQuery(query).getSingleResult();
        } catch(NoResultException ex) { 
            // NoResultException - if there is no result
            LOG.log(Level.WARNING, "### findByProperty() NO RESULT EXCEPTION:{0}", ex.getMessage());
            throw new NotFoundException(ex);
        } catch(NonUniqueResultException | IllegalStateException | 
                QueryTimeoutException | TransactionRequiredException | 
                PessimisticLockException | LockTimeoutException ex) { 
            // NonUniqueResultException - if more than one result
            // IllegalStateException - if called for a Java Persistence query language UPDATE or DELETE statement
            // QueryTimeoutException - if the query execution exceeds the query timeout value set and only the statement is rolled back
            // TransactionRequiredException - if a lock mode has been set and there is no transaction
            // PessimisticLockException - if pessimistic locking fails and the transaction is rolled back
            // LockTimeoutException - if pessimistic locking fails and only the statement is rolled back
            LOG.log(Level.WARNING, "### findByEmfindByPropertyail() EXCEPTION:{0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        } catch(PersistenceException ex) {
            // PersistenceException - if the query execution exceeds the query timeout value set and the transaction is rolled back            
            LOG.log(Level.WARNING, "### findByProperty() PERSISTENCE EXCEPTION:{0}", ex.getMessage());
            throw new InternalServerErrorException(ex);
        } 
        
    }
    
    // REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST 
    //   REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST REQUEST 
    
    protected <T> T request(URI uri, Class<T> className) 
                        throws NotFoundException, InternalServerErrorException {
        T found = null;
        Response response = null;
        try {
            response = get(uri);
            if(response.getStatus() == Response.Status.OK.getStatusCode())
                found = response.readEntity(className);
            // ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR 
            else if(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new NotFoundException();
            } else {
                Response.StatusType info = response.getStatusInfo();
                LOG.log(Level.SEVERE, "### request() PROBLEM:{0} {1}",
                        new Object[] {response.getStatus(),
                            info != null ? info.getReasonPhrase() : "<NO REASON GIVEN>"});
            }
            
        } finally {
            if(response != null) response.close();
        }
        
        return found;
    }
    
    
    protected Response get(URI uri) {
        return ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get();
    }
    
    protected <T> Response post(URI uri, T t) {
        return ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).post(Entity.json(t));
    }
    
    protected <T> Response put(URI uri, T t) {
        return ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).put(Entity.json(t));
    }
    
    protected <T> Response delete(URI uri, T t) {
        return ClientBuilder.newClient().target(uri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).delete();
    }
    
    // MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 
    //   MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING MESSAGING 
    protected void sendAsyncMessage(String json) throws JMSException {
        Connection connection = null; Session session = null;
        try {
            connection = messaging.connection(); connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = messaging.topic();
            
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            TextMessage textMessage = session.createTextMessage(json);
            producer.send(textMessage);
            
        } finally {
            if(session != null) try{session.close();}catch(JMSException e){}
            if(connection != null) try{connection.close();}catch(JMSException e){}
        }
    }
}
