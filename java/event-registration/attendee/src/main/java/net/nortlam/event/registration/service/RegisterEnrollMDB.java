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
import net.nortlam.event.registration.entity.Enroll;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
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
    private ServiceEnroll service;

    @Override
    public void onMessage(Message message) {
        LOG.log(Level.INFO, ">>> onMessage()");
        
        if(message instanceof TextMessage) {
            try {
                String json = ((TextMessage)message).getText();
                Enroll enroll = new Enroll(json);
                LOG.log(Level.INFO, ">>> onMessage Attendee is enrolled ito an Event");
                service.create(enroll);
                
            } catch(IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "### ILLEGAL AGUMENT EXCEPTION:{0}", ex.getMessage());
            } catch(BiggerException ex) {
                LOG.log(Level.SEVERE, "### BIGGER EXCEPTION:{0}", ex.getMessage());
            } catch(MissingInformationException ex) {
                LOG.log(Level.SEVERE, "### MISSING INFORMATION EXCEPTION:{0}", ex.getMessage());
            } catch(AlreadyExistsException ex) {
                LOG.log(Level.SEVERE, "### ALREADY EXISTING EXCEPTION:{0}", ex.getMessage());
            } catch(InternalServerErrorException ex) {
                LOG.log(Level.SEVERE, "### INTERNAL SERVER ERROR EXCEPTION:{0}", ex.getMessage());
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
