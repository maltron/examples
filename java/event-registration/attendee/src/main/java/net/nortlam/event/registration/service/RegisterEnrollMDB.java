package net.nortlam.event.registration.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
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
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = Messaging.TOPIC), 
        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
public class RegisterEnrollMDB implements MessageListener {

    private static final Logger LOG = Logger.getLogger(RegisterEnrollMDB.class.getName());
    
    @EJB
    private Service service;

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                String json = ((TextMessage)message).getText();
                LOG.log(Level.INFO, ">>> onMessage() Message:{0}", json);
                Order order = new Order(json);
                LOG.log(Level.INFO, ">>> onMessage() Attendee is ordered");
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
