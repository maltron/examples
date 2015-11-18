package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.text.ParseException;
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
import net.nortlam.event.registration.util.DateUtil;

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
    
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final int LENGTH_FIRST_NAME = 40;
    @Column(name="FIRST_NAME", length = LENGTH_FIRST_NAME, nullable = false)
    @XmlElement(name=COLUMN_FIRST_NAME, type=String.class, required=true)
    private String firstName;
    
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final int LENGTH_LAST_NAME = 40;
    @Column(name="LAST_NAME", length = LENGTH_LAST_NAME, nullable = false)
    @XmlElement(name=COLUMN_LAST_NAME, type=String.class, required=true)
    private String lastName;

    public static final String COLUMN_EMAIL = "email";
    public static final int LENGTH_EMAIL = 120;
    @Column(name="EMAIL", length = LENGTH_EMAIL, nullable = false, unique = true)
    @XmlElement(name=COLUMN_EMAIL, type=String.class, required=true)
    private String email;
    
    public static final String COLUMN_ORDER_ITEMS = "items";
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="ORDER_HAS_ITEMS",
            joinColumns=@JoinColumn(name="ORDER_ID", referencedColumnName="ORDER_ID"),
            inverseJoinColumns=@JoinColumn(name="ORDER_ITEM_ID", referencedColumnName="ORDER_ITEM_ID"))
    @XmlElements({@XmlElement(name = COLUMN_ORDER_ITEMS, type = OrderItem.class, required = false)})
    private Collection<OrderItem> items;
    
    public static final String COLUMN_TOTAL_ITEMS = "totalItems";
    @Column(name="TOTAL_ITEMS", nullable = false)
    @XmlElement(name=COLUMN_TOTAL_ITEMS, type=int.class, required=true)
    private int totalItems;

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
        this.totalItems = 0;
        this.items = new ArrayList<OrderItem>();
        for(Ticket ticket: event.getTickets()) {
            // Only those that has a quantitySelected > 0
            if(ticket.getQuantitySelected() > 0) {
                this.totalItems += ticket.getQuantitySelected();
                this.items.add(new OrderItem(ticket));
            }
        }
        
        this.attendeeID = attendee.getID();
        this.firstName = attendee.getFirstName();
        this.lastName = attendee.getLastName();
        this.email = attendee.getEmail();
        
        this.eventID = event.getID();
        
        this.orderPlaced = new Date();
        this.title = event.getTitle();
        this.starts = event.getStarts();
    }
    
    public Order(JsonObject object) throws JsonException, JsonParsingException,
                                                        IllegalStateException {
        try {
            try {
                this.ID = object.getInt(COLUMN_ID);
            } catch(NullPointerException ex) {
                this.ID = 0;
            }
            
            try {
                this.eventID = object.getInt(COLUMN_EVENT);
            } catch(NullPointerException ex) {
                this.eventID = 0;
            }
            
            try {
                this.attendeeID = object.getInt(COLUMN_ATTENDEE);
            } catch(NullPointerException ex) {
                this.attendeeID = 0;
            }
            
            try {
                this.firstName = object.getString(COLUMN_FIRST_NAME);
            } catch(NullPointerException ex) {
                this.firstName = null;
            }
            
            try {
                this.lastName = object.getString(COLUMN_LAST_NAME);
            } catch(NullPointerException ex) {
                this.lastName = null;
            }
            
            try {
                this.email = object.getString(COLUMN_EMAIL);
            } catch(NullPointerException ex) {
                this.email = null;
            }
            
            // Items
            JsonArray arrayOrderItems = object.getJsonArray(COLUMN_ORDER_ITEMS);
            if(arrayOrderItems != null && !arrayOrderItems.isEmpty()) {
                this.items = new ArrayList<OrderItem>();
                for(int i=0; i < arrayOrderItems.size(); i++) 
                    items.add(new OrderItem(arrayOrderItems.getJsonObject(i)));
            }
            
            try {
                this.totalItems = object.getInt(COLUMN_TOTAL_ITEMS);
            } catch(NullPointerException ex) {
                this.totalItems = 0;
            }
            
            try {
                this.orderPlaced = DateUtil.toDate(object.getString(COLUMN_ORDER_PLACED));
            } catch(NullPointerException ex) {
                this.orderPlaced = null;
            } catch(ParseException ex) {
                LOG.log(Level.WARNING, "### PARSE EXCEPTION: Unable to Parse Date:{0}",
                        ex.getMessage());
                this.orderPlaced = null;
            }
            
            try {
                this.title = object.getString(COLUMN_TITLE);
            } catch(NullPointerException ex) {
                this.title = null;
            }
            
            try {
                this.starts = DateUtil.toDate(object.getString(COLUMN_STARTS));
            } catch(NullPointerException ex) {
                this.starts = null;
            } catch(ParseException ex) {
                LOG.log(Level.WARNING, "### PARSE EXCEPTION: Unable to Parse Date:{0}",
                        ex.getMessage());
                this.starts = null;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
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
        hash = 67 * hash + Objects.hashCode(this.firstName);
        hash = 67 * hash + Objects.hashCode(this.lastName);
        hash = 67 * hash + Objects.hashCode(this.email);
        hash = 67 * hash + Objects.hashCode(this.items);
        hash = 67 * hash + this.totalItems;
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
        if (this.totalItems != other.totalItems) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
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
        if(eventID > 0) builder.add(COLUMN_EVENT, eventID);
        if(attendeeID > 0) builder.add(COLUMN_ATTENDEE, attendeeID);
        if(firstName != null)
            builder.add(COLUMN_FIRST_NAME, firstName);
        if(lastName != null)
            builder.add(COLUMN_LAST_NAME, lastName);
        if(email != null)
            builder.add(COLUMN_EMAIL, email);
        
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
            builder.add(COLUMN_ORDER_ITEMS, arrayOrderItems);
        }
        
        if(orderPlaced != null) 
            builder.add(COLUMN_ORDER_PLACED, DateUtil.toString(orderPlaced));
        if(title != null) 
            builder.add(COLUMN_TITLE, title);
        if(starts != null)
            builder.add(COLUMN_STARTS, DateUtil.toString(starts));
        
        return builder.build().toString();
    }
}
