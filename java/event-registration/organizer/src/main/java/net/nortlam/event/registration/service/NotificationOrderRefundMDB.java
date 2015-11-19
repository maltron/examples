package net.nortlam.event.registration.service;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.jms.Messaging;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = Messaging.TOPIC_REFUND), 
        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
public class NotificationOrderRefundMDB implements MessageListener {

    private static final Logger LOG = Logger.getLogger(NotificationOrderRefundMDB.class.getName());
    
    @EJB
    private Service service;

    @Override
    public void onMessage(Message message) {
        LOG.log(Level.INFO, ">>> [ORGANIZER] onMessage()");
        if(message instanceof TextMessage) {
            try {
                String value = ((TextMessage)message).getText();
                JsonReader reader = Json.createReader(new StringReader(value));
                Order order = new Order(reader.readObject());
                service.delete(order);
                
            } catch(IllegalArgumentException | TransactionRequiredException ex) {
                LOG.log(Level.SEVERE, "### ILLEGAL ARGUMENT | TRANSACTION"+
                                        " REQUIRED EXCEPTION:{0}", ex.getMessage());
            } catch(JMSException ex) {
                LOG.log(Level.SEVERE, "### JMS EXCEPTION:{0}", ex.getMessage());
            } catch(JsonException ex) {
                LOG.log(Level.SEVERE, "### JMS EXCEPTION:{0}", ex.getMessage());
            } catch(IllegalStateException ex) {
                LOG.log(Level.SEVERE, "### ILLEGAL STATE EXCEPTION:{0}", ex.getMessage());
            }
        }
        
    }
}
