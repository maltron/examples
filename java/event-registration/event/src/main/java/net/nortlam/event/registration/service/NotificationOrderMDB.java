package net.nortlam.event.registration.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.JsonException;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.jms.Messaging;

/**
 * For each Order, decrease the number available for an event 
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = Messaging.TOPIC_ORDER_INFORMATION),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = Messaging.TOPIC_ORDER), 
        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
public class NotificationOrderMDB implements MessageListener {

    private static final Logger LOG = Logger.getLogger(NotificationOrderMDB.class.getName());
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                String json = ((TextMessage)message).getText();
                LOG.log(Level.INFO, ">>> onMessage() Message:{0}", json);
                Order order = new Order(json);
                
                
                
            } catch(EntityExistsException | 
                    IllegalArgumentException | TransactionRequiredException ex) {
                LOG.log(Level.SEVERE, 
                        "### ENTITY EXISTS | ILLEGAL | TRANSACTION REQUIRED:{0}",
                                                                ex.getMessage());
            } catch(JMSException ex) {
                LOG.log(Level.SEVERE, "### JMS EXCEPTION:{0}", ex.getMessage());
            } catch(JsonException ex) {
                LOG.log(Level.SEVERE, "### JSON EXCEPTION:{0}", ex.getMessage());
            } catch(IllegalStateException ex) {
                LOG.log(Level.SEVERE, "### ILLEGAL STATE EXCEPTION:{0}", ex.getMessage());
            }
        }
        
        
    }
    
    

}
