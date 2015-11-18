package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="OrderItem")
@Table(name="ORDER_ITEM")
@XmlRootElement(name="orderItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItem implements Serializable {

    private static final Logger LOG = Logger.getLogger(OrderItem.class.getName());

    public static final String COLUMN_ID = "id";
    @Id
    @Column(name="ORDER_ITEM_ID")
    @XmlAttribute(name=COLUMN_ID, required=false)
    private long ID;
    
    public static final String COLUMN_TICKET = "ticketID";
    @Column(name="TICKET_ID", nullable = false)
    @XmlElement(name=COLUMN_TICKET, type=long.class, required=true)
    private long ticketID;
    
    public static final String COLUMN_QUANTITY = "quantity";
    @Column(name="QUANTITY", nullable = false)
    @XmlElement(name=COLUMN_QUANTITY, type=int.class, required=true)
    private int quantity;
    
    public OrderItem() {
    }
    
    public OrderItem(Ticket ticket) {
        setTicketID(ticket.getID());
        setQuantity(ticket.getQuantitySelected());
    }
    
    public OrderItem(String json) throws JsonException, JsonParsingException,
                                                        IllegalStateException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject object = reader.readObject();
        try {
            try {
                setID(object.getInt(COLUMN_ID));
            } catch(NullPointerException ex) {
                setID(0);
            }
            
            try {
                setTicketID(object.getInt(COLUMN_TICKET));
            } catch(NullPointerException ex) {
                setTicketID(0);
            }
            
            try {
                setQuantity(object.getInt(COLUMN_QUANTITY));
            } catch(NullPointerException ex) {
                setQuantity(0);
            }
            
        } catch(ClassCastException ex) {
            LOG.log(Level.SEVERE, "### CLASS CAST EXCEPTION:{0}", ex.getMessage());
        }
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
//        StringBuilder builder = new StringBuilder();
//        builder.append("<OrderItem ID=\"").append(ID).append("\">");
//        builder.append("<Ticket ID=\"").append(ticketID > 0 ? ticketID : "ZERO")
//                .append("\"/>");
//        builder.append("<Quantity>").append(quantity > 0 ? quantity : "ZERO")
//                .append("</Quantity>");
//        builder.append("</OrderItem>");
//        
//        return builder.toString();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if(ID > 0) builder.add(COLUMN_ID, ID); // required=false
        builder.add(COLUMN_TICKET, ticketID);
        builder.add(COLUMN_QUANTITY, quantity);
        
        return builder.build().toString();
    }
}
