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
        LOG.log(Level.INFO, ">>> [EVENT] onMessage()");
        if(message instanceof TextMessage) {
            try {
                String value = ((TextMessage)message).getText();
                JsonReader reader = Json.createReader(new StringReader(value));
                Order order = new Order(reader.readObject());
                // Before deleting Order, one need to adjust Event's remaining tickets
                Event event = service.read(order.getEventID());
                for(Ticket ticket: event.getTickets())
                    for(OrderItem item: order.getItems()) {
                        if(item.getTicketID() == ticket.getID()) {
                            // Put back the tickets available
                            int refund = ticket.getQuantityAvailable()
                                                            +item.getQuantity();
                            ticket.setQuantityAvailable(refund);
                        }
                    }
                
                // Update Number of attendees for this event
                event.setRegisteredAttendees(service.countOrdersFor(event));

                // Update the current number of available tickets
                long totalRemaining = 0;
                for(Ticket ticket: event.getTickets())
                    totalRemaining += ticket.getQuantityAvailable();

                event.setRemainingTickets(totalRemaining);
                service.update(event);
                
                // Finally, delete Order
                service.delete(order);
            } catch(BiggerException | MissingInformationException | 
                                                AlreadyExistsException ex) {
                LOG.log(Level.SEVERE, "### BIGGER | MISSING INFORMATION | "+
                        " ALREADY EXISTS EXCEPTION:{0}", ex.getMessage());                                ;;
            } catch(NotFoundException ex) {
                LOG.log(Level.SEVERE, "### NOT FOUND EXCEPTION:{0}", ex.getMessage());
            } catch(InternalServerErrorException ex) {
                LOG.log(Level.SEVERE, "### INTERNAL SERVER ERROR EXCEPTION:{0}",
                                                                ex.getMessage());
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
