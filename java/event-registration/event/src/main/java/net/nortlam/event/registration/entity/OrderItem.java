package net.nortlam.event.registration.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="OrderItem")
@Table(name="ORDER_ITEM")
public class OrderItem implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ITEM_ID")
    private long ID;
    
    @Column(name="TICKET_ID", nullable = false)
    private long ticketID;
    
    @Column(name="QUANTITY", nullable = false)
    private int quantity;
    
    public OrderItem() {
    }
    
    public OrderItem(Ticket ticket) {
        setTicketID(ticket.getID());
        setQuantity(ticket.getQuantitySelected());
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getTicketID() {
        return ticketID;
    }

    public void setTicketID(long ticketID) {
        this.ticketID = ticketID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 53 * hash + (int) (this.ticketID ^ (this.ticketID >>> 32));
        hash = 53 * hash + this.quantity;
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
        final OrderItem other = (OrderItem) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.ticketID != other.ticketID) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<OrderItem ID=\"").append(ID).append("\">");
        builder.append("<Ticket ID=\"").append(ticketID > 0 ? ticketID : "ZERO")
                .append("\"/>");
        builder.append("<Quantity>").append(quantity > 0 ? quantity : "ZERO")
                .append("</Quantity>");
        builder.append("</OrderItem>");
        
        return builder.toString();
    }
}
