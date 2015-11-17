package net.nortlam.event.registration.jms.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Default;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.nortlam.event.registration.jms.Messaging;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Default
public class MessagingActiveMQ implements Messaging {
    
    private static final Logger LOG = Logger.getLogger(MessagingActiveMQ.class.getName());
    
    @Override
    public Connection connection() {
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory)context.lookup(FACTORY_INFORMATION);
            connection = factory.createConnection(); connection.start();
            
        } catch(NamingException ex) {
            LOG.log(Level.SEVERE, "### connection() NAMING EXCEPTION {0}", 
                                                                ex.getMessage());
        } catch(JMSException ex) {
            LOG.log(Level.SEVERE, "### connection() JMS EXCEPTION {0}", 
                                                                ex.getMessage());
        }
        
        assert connection != null;
        return connection;
    }

    @Override
    public Topic topicOrder() {
        Topic topic = null;
        try {
            InitialContext context = new InitialContext();
            topic = (Topic)context.lookup(TOPIC_ORDER_INFORMATION);
            
        } catch(NamingException ex) {
            LOG.log(Level.SEVERE, "### topic() NAMING EXCEPTION {0}", 
                                                                ex.getMessage());
        } 
        
        assert topic != null;
        return topic;
    }
}
