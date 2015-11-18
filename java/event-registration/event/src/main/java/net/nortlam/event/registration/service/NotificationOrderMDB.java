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
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.entity.OrderItem;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
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
    
    @EJB
    private Service service;
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                String json = ((TextMessage)message).getText();
                LOG.log(Level.INFO, ">>> [EVENT] onMessage() Message:{0}", json);
                Order order = new Order(json);
                
                try {
                    Event event = service.read(order.getEventID());
                    for(OrderItem orderItem: order.getItems())
                        for(Ticket ticket: event.getTickets()) {
                            // Subtract number of available tickets
                            // Based on the order
                            if(orderItem.getTicketID() == ticket.getID()) {
                                int remaining = ticket.getQuantityAvailable() -
                                        orderItem.getQuantity();
                                ticket.setQuantityAvailable(remaining);
                            }
                        }
                    
                    long totalRemaining = 0;
                    for(Ticket ticket: event.getTickets())
                        totalRemaining += ticket.getQuantityAvailable();
                    
                    event.setRemainingTickets(totalRemaining);
                    service.update(event);
                            
                } catch(IllegalArgumentException | 
                        BiggerException | MissingInformationException | 
                                                    AlreadyExistsException ex) {
                    // Due Update operation
                    LOG.log(Level.SEVERE, "### ILLEGAL ARGUMENT | BIGGER | "+
                            "MISSING INFORMATION | ALREADY EXISTS EXCEPTION:"+
                            " {0}", ex.getMessage());
                    
                } catch(NotFoundException ex) {
                    // Very unlike to happen
                    LOG.log(Level.SEVERE, "### NOT FOUND EXCEPTION:{0}", ex.getMessage());
                } catch(InternalServerErrorException ex) {
                    LOG.log(Level.SEVERE, "### INTERNAL SERVER ERROR EXCEPTION:"+
                            " {0}", ex.getMessage());
                }
                
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
