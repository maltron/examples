package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the information regarding the sell of tickets
 * 
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="Order")
@Table(name="EVENT_ORDER", uniqueConstraints = 
        @UniqueConstraint(name="ORDER_SPECIFIC_TO_ATTENDEE", 
                        columnNames = {"EVENT_ID", "ATTENDEE_ID"}))
@XmlRootElement(name="Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {

    private static final Logger LOG = Logger.getLogger(Order.class.getName());
    
    public static final String COLUMN_ID = "id";
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    @XmlAttribute(name=COLUMN_ID, required = true)
    private long ID;

    public static final String COLUMN_EVENT = "eventID";
    @Column(name="EVENT_ID", nullable = false)
    @XmlElement(name=COLUMN_EVENT, type=long.class, required=true)
    private long eventID;
    
    public static final String COLUMN_ATTENDEE = "attendeeID";
    @Column(name="ATTENDEE_ID", nullable = false)
    @XmlElement(name=COLUMN_ATTENDEE, type=long.class, required=true)
    private long attendeeID;
    
    public static final String COLUMN_TICKETS = "items";
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="ORDER_HAS_ITEMS",
            joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ORDER_ID"),
            inverseJoinColumns=@JoinColumn(name="ORDER_ITEM_ID", referencedColumnName="ORDER_ITEM_ID"))
    @XmlElements({@XmlElement(name = COLUMN_TICKETS, type = OrderItem.class, required = false)})
    private Collection<OrderItem> items;

    public static final String COLUMN_ORDER_PLACED = "orderPlaced";
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ORDER_PLACED", nullable = false)
    @XmlElement(name=COLUMN_ORDER_PLACED, type=Date.class, required=true)
    private Date orderPlaced; // When the Order's was placed

    public static final String COLUMN_TITLE = "title";
    @Column(name="TITLE", length = Event.LENGTH_TITLE, nullable = true)
    @XmlElement(name=COLUMN_TITLE, type=String.class, required=false)
    private String title; // Event's Title
    
    public static final String COLUMN_STARTS = "starts";
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EVENT_STARTS", nullable = true)
    @XmlElement(name=COLUMN_STARTS, type=Date.class, required=false)
    private Date starts; // When the Event Starts

    public Order() {
    }
    
    public Order(Event event, Attendee attendee) {
        // Create the items for this particular Order
        Collection<OrderItem> items = new ArrayList<OrderItem>();
        for(Ticket ticket: event.getTickets())
            items.add(new OrderItem(ticket));
        setItems(items);
        
        setAttendeeID(attendee.getID());
        setEventID(event.getID());
        
        setOrderPlaced(new Date());
        setTitle(event.getTitle());
        setStarts(event.getStarts());
    }
    
    public Order(String json) throws JsonException, JsonParsingException,
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
                setEventID(object.getInt(COLUMN_EVENT));
            } catch(NullPointerException ex) {
                setEventID(0);
            }
            
            try {
                setAttendeeID(object.getInt(COLUMN_ATTENDEE));
            } catch(NullPointerException ex) {
                setAttendeeID(0);
            }
            
            // Items
            JsonArray arrayOrderItems = object.getJsonArray(COLUMN_TICKETS);
            if(arrayOrderItems != null) {
                Collection<OrderItem> items = new ArrayList<OrderItem>();
                for(int i=0; i < arrayOrderItems.size(); i++) {
                    JsonObject objectOrderItem = arrayOrderItems.getJsonObject(i);
                    OrderItem orderItem = new OrderItem();
                    
                    try {
                        orderItem.setID(object.getInt(OrderItem.COLUMN_ID));
                    } catch(NullPointerException ex) {
                        orderItem.setID(0);
                    }

                    try {
                        orderItem.setTicketID(object.getInt(OrderItem.COLUMN_TICKET));
                    } catch(NullPointerException ex) {
                        orderItem.setTicketID(0);
                    }

                    try {
                        orderItem.setQuantity(object.getInt(OrderItem.COLUMN_QUANTITY));
                    } catch(NullPointerException ex) {
                        orderItem.setQuantity(0);
                    }
                    items.add(orderItem);
                }
                setItems(items);
            }
            
            try {
                setOrderPlaced(new Date(object.getInt(COLUMN_ORDER_PLACED)));
            } catch(NullPointerException ex) {
                setOrderPlaced(null);
            }
            
            try {
                setTitle(object.getString(COLUMN_TITLE));
            } catch(NullPointerException ex) {
                setTitle(null);
            }
            
            try {
                setStarts(new Date(object.getInt(COLUMN_STARTS)));
            } catch(NullPointerException ex) {
                setStarts(null);
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

    public Date getOrderPlaced() {
        return orderPlaced;
    }

    public void setOrderPlaced(Date orderPlaced) {
        this.orderPlaced = orderPlaced;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStarts() {
        return starts;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 67 * hash + (int) (this.eventID ^ (this.eventID >>> 32));
        hash = 67 * hash + (int) (this.attendeeID ^ (this.attendeeID >>> 32));
        hash = 67 * hash + Objects.hashCode(this.items);
        hash = 67 * hash + Objects.hashCode(this.orderPlaced);
        hash = 67 * hash + Objects.hashCode(this.title);
        hash = 67 * hash + Objects.hashCode(this.starts);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
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
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        if (!Objects.equals(this.orderPlaced, other.orderPlaced)) {
            return false;
        }
        if (!Objects.equals(this.starts, other.starts)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if(ID > 0) builder.add(COLUMN_ID, ID);
        builder.add(COLUMN_EVENT, eventID);
        builder.add(COLUMN_ATTENDEE, attendeeID);
        
        // Items
        if(items != null) {
            JsonArrayBuilder arrayOrderItems = Json.createArrayBuilder();
            for(OrderItem orderItem: items) {
                JsonObjectBuilder builderItem = Json.createObjectBuilder();
                if(orderItem.getID() > 0)
                    builderItem.add(OrderItem.COLUMN_ID, orderItem.getID());
                builderItem.add(OrderItem.COLUMN_TICKET, orderItem.getTicketID());
                builderItem.add(OrderItem.COLUMN_QUANTITY, orderItem.getQuantity());
                
               arrayOrderItems.add(builderItem);
            }
            builder.add(COLUMN_TICKETS, arrayOrderItems);
        }
        
        if(orderPlaced != null) 
            builder.add(COLUMN_ORDER_PLACED, orderPlaced.getTime());
        if(title != null) 
            builder.add(COLUMN_TITLE, title);
        if(starts != null)
            builder.add(COLUMN_STARTS, starts.getTime());
        
        return builder.toString();
    }
}
