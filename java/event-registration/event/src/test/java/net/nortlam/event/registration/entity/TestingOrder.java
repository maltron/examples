package net.nortlam.event.registration.entity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public class TestingOrder {
    
    @Test
    public void testingOrderJSON() {
        Event event = new Event();
        event.setDesignation("javaone");
        event.setEdition(2015);
        event.setTitle("JavaOne 2015 - The Largest Java Event");
        event.setLocation("Moscone Conference Center");
        event.setAddress("5677 Maple Street");
        event.setCity("San Francisco");
        event.setRegion("California");
        event.setZipCode("95664");
        event.setCountry("USA");
        
        Calendar starts = Calendar.getInstance();
        starts.set(2015, 11, 18, 12, 00);
        event.setStarts(starts.getTime());
        
        Calendar ends = Calendar.getInstance();
        ends.set(2015, 11, 23, 18, 00);
        event.setEnds(ends.getTime());
        
        event.setDescription("A simple example of a description of an event");
        event.setOrganizer(1);
        event.setRemainingTickets(28);
        
            Ticket ticketMale = new Ticket("MALE", 10); 
            ticketMale.setQuantitySelected(5);
            Ticket ticketFemale = new Ticket("FEMALE", 20);
            ticketFemale.setQuantitySelected(0);
            Set<Ticket> tickets = new HashSet<Ticket>(2);
            tickets.add(ticketMale);
            tickets.add(ticketFemale);
        event.setTickets(tickets);
        
        Attendee attendee = new Attendee("Mauricio", "Leal", "maltron@gmail.com", "123");
        
        Order newOrder = new Order(event, attendee);
        System.out.println(">>> Order:"+newOrder.toString());
        
        
        
    }

}
