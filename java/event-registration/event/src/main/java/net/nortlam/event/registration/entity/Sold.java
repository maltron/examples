package net.nortlam.event.registration.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the information regarding the sell of tickets
 * 
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="Sold")
@Table(name="SOLD")
public class Sold implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SOLD_ID")
    private long ID;
    
    @Column(name="EVENT_ID", nullable = false)
    private long eventID;
    
    @Column(name="ATTENDEE_ID", nullable = false)
    private long attendeeID;
    
    @Column(name="TICKET_ID", nullable = false)
    private long ticketID;
    
    @Column(name="QUANTITY_SOLD", nullable = false)
    private int quantitySold;

    public Sold() {
    }
    
    public Sold(Event event, Attendee attendee) {
        
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public long getAttendeeID() {
        return attendeeID;
    }

    public void setAttendeeID(long attendeeID) {
        this.attendeeID = attendeeID;
    }

    public long getTicketID() {
        return ticketID;
    }

    public void setTicketID(long ticketID) {
        this.ticketID = ticketID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 41 * hash + (int) (this.eventID ^ (this.eventID >>> 32));
        hash = 41 * hash + (int) (this.attendeeID ^ (this.attendeeID >>> 32));
        hash = 41 * hash + (int) (this.ticketID ^ (this.ticketID >>> 32));
        hash = 41 * hash + this.quantitySold;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sold other = (Sold) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.eventID != other.eventID) {
            return false;
        }
        if (this.attendeeID != other.attendeeID) {
            return false;
        }
        if (this.ticketID != other.ticketID) {
            return false;
        }
        if (this.quantitySold != other.quantitySold) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Sold ID=\"").append(ID).append("\">");
        builder.append("<Event ID=\"").append(eventID > 0 ? eventID : "ZERO")
                .append("\"/>");
        builder.append("<Attendee ID=\"").append(attendeeID > 0 ? attendeeID : "ZERO")
                .append("\"/>");
        builder.append("<Ticket ID=\"").append(ticketID > 0 ? ticketID : "ZERO")
                .append("\"/>");
        builder.append("<QuantitySold>").append(quantitySold > 0 ? quantitySold : "ZERO")
                .append("</QuantitySold>");
        builder.append("</Sold>");
        
        return builder.toString();
    }
}
