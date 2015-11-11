package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * This is the information regarding the sell of tickets
 * 
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="Order")
@Table(name="EVENT_ORDER", uniqueConstraints = 
        @UniqueConstraint(name="ORDER_SPECIFIC_TO_ATTENDEE", 
                        columnNames = {"EVENT_ID", "ATTENDEE_ID"}))
public class Order implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    private long ID;
    
    @Column(name="EVENT_ID", nullable = false)
    private long eventID;
    
    @Column(name="ATTENDEE_ID", nullable = false)
    private long attendeeID;
    
    public static final String COLUMN_TICKETS = "items";
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="ORDER_HAS_ITEMS",
            joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ORDER_ID"),
            inverseJoinColumns=@JoinColumn(name="ORDER_ITEM_ID", referencedColumnName="ORDER_ITEM_ID"))
    @XmlElements({@XmlElement(name = COLUMN_TICKETS, type = OrderItem.class, required = false)})
    private Collection<OrderItem> items;

    public Order() {
    }
    
    public Order(Event event, Attendee attendee) {
        // Create the items for this particular Order
        Collection<OrderItem> items = new ArrayList<OrderItem>();
        for(Ticket ticket: event.getTickets())
            items.add(new OrderItem(ticket));
        setItems(items);
        
        setEventID(event.getID());
        setAttendeeID(attendee.getID());
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

    public Collection<OrderItem> getItems() {
        return items;
    }

    public void setItems(Collection<OrderItem> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 89 * hash + (int) (this.eventID ^ (this.eventID >>> 32));
        hash = 89 * hash + (int) (this.attendeeID ^ (this.attendeeID >>> 32));
        hash = 89 * hash + Objects.hashCode(this.items);
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
        final Order other = (Order) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.eventID != other.eventID) {
            return false;
        }
        if (this.attendeeID != other.attendeeID) {
            return false;
        }
        if (!Objects.equals(this.items, other.items)) {
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
        if(items != null) 
            for(OrderItem item: items)
                builder.append("<Items>").append(item.toString()).append("</Items>");
        
        builder.append("</Sold>");
        
        return builder.toString();
    }
}
