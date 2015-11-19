package net.nortlam.event.registration.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
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
import javax.json.JsonObject;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.jms.Messaging;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = Messaging.TOPIC_ORDER), 
        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
public class NotificationOrderMDB implements MessageListener {

    private static final Logger LOG = Logger.getLogger(NotificationOrderMDB.class.getName());
    
    public static final SimpleDateFormat DATE_FORMAT = 
                                new SimpleDateFormat("EEEE,   MMM d, yyyy 'at' HH:mm (z)");
    @EJB
    private Service service;

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                String json = ((TextMessage)message).getText();
                LOG.log(Level.INFO, ">>> [ATTENDEE] onMessage() Message:{0}", json);
                JsonObject object = Json.createReader(new StringReader(json)).readObject();
                Order order = new Order(object);
                LOG.log(Level.INFO, ">>> [ATTENDEE] onMessage() STARTS:{0}", DATE_FORMAT.format(order.getStarts()));
                
                LOG.log(Level.INFO, ">>> [ATTENDEE] onMessage() Attendee is ordered");
                service.save(order);
                
            } catch(EntityExistsException | 
                    IllegalArgumentException | TransactionRequiredException ex) {
                LOG.log(Level.SEVERE, 
                        "### ENTITY EXISTS | ILLEGAL | TRANSACTION REQUIRED:{0}",
                                                                ex.getMessage());
//            } catch(IllegalArgumentException ex) {
//                LOG.log(Level.SEVERE, "### ILLEGAL AGUMENT EXCEPTION:{0}", ex.getMessage());
//            } catch(BiggerException ex) {
//                LOG.log(Level.SEVERE, "### BIGGER EXCEPTION:{0}", ex.getMessage());
//            } catch(MissingInformationException ex) {
//                LOG.log(Level.SEVERE, "### MISSING INFORMATION EXCEPTION:{0}", ex.getMessage());
//            } catch(AlreadyExistsException ex) {
//                LOG.log(Level.SEVERE, "### ALREADY EXISTING EXCEPTION:{0}", ex.getMessage());
//            } catch(InternalServerErrorException ex) {
//                LOG.log(Level.SEVERE, "### INTERNAL SERVER ERROR EXCEPTION:{0}", ex.getMessage());
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
