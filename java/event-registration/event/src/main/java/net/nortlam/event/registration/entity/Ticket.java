package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Objects;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="Ticket")
@Table(name="TICKET")
public class Ticket implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(Ticket.class.getName());

    public static final String COLUMN_ID = "id";
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private long ID;
    
    public static final String COLUMN_NAME = "name";
    public static final int LENGTH_NAME = 40;
    @Column(name="NAME", columnDefinition = "VARCHAR(40)", length = LENGTH_NAME, nullable = false)
    private String name;
    
    public static final String COLUMN_QUANTITY_AVAILABLE = "quantityAvailable";
    @Column(name="QUANTITY_AVAILABLE", columnDefinition = "INT", nullable = false)
    private int quantityAvailable;
    
    public static final String COLUMN_QUANTITY_SELECTED = "quantitySelected";
    @Transient
//    @Column(name="QUANTITY_SELECTED", columnDefinition = "INT", nullable = true)
    private int quantitySelected;

    public Ticket() {
    }

    public Ticket(String name, int quantityAvailable) {
        this.name = name;
        this.quantityAvailable = quantityAvailable;
    }
    
    public Ticket(String json) throws JsonException, JsonParsingException,
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
                setName(object.getString(COLUMN_NAME));
            } catch(NullPointerException ex) {
                setName(null);
            }
            
            try {
                setQuantityAvailable(object.getInt(COLUMN_QUANTITY_AVAILABLE));
            } catch(NullPointerException ex) {
                setQuantityAvailable(0);
            }
            
            try {
                setQuantitySelected(object.getInt(COLUMN_QUANTITY_SELECTED));
            } catch(NullPointerException ex) {
                setQuantitySelected(0);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantitySelected() {
        return quantitySelected;
    }

    public void setQuantitySelected(int quantitySelected) {
        this.quantitySelected = quantitySelected;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.quantityAvailable;
        hash = 59 * hash + this.quantitySelected;
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
        final Ticket other = (Ticket) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.quantityAvailable != other.quantityAvailable) {
            return false;
        }
        if (this.quantitySelected != other.quantitySelected) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("<Ticket ID=\"").append(ID).append("\">");
//        builder.append("<Name>").append(name != null ? name : "NULl").append("</Name>");
//        builder.append("<QuantityAvailable>").append(quantityAvailable > 0 ? 
//                quantityAvailable : "ZERO")
//                .append("</QuantityAvailable>");
//        builder.append("<QuantitySelected>").append(quantitySelected > 0 ?
//                quantitySelected : "ZERO")
//                .append("</QuantitySelected>");
//        
//        builder.append("</Ticket>");
//        
//        return builder.toString();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if(ID > 0) builder.add(COLUMN_ID, ID); // required=false
        builder.add(COLUMN_NAME, name);
        builder.add(COLUMN_QUANTITY_AVAILABLE, quantityAvailable);
        if(quantitySelected > 0) // required=false
            builder.add(COLUMN_QUANTITY_SELECTED, quantitySelected);
        
        return builder.toString();
    }
}
