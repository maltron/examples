package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@Entity(name="Ticket")
//@Table(name="TICKET")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ticket implements Serializable {

    public static final String COLUMN_ID = "id";
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "TICKET_ID")
    @XmlAttribute(name = COLUMN_ID, required=false)
    private long ID;
    
    public static final String COLUMN_NAME = "name";
    public static final int LENGTH_NAME = 40;
//    @Column(name="NAME", columnDefinition = "VARCHAR(40)", length = LENGTH_NAME, nullable = false)
    @XmlElement(name=COLUMN_NAME, type=String.class, required=true)
    private String name;
    
    public static final String COLUMN_QUANTITY = "quantity";
//    @Column(name="QUANTITY", columnDefinition = "INT", nullable = false)
    @XmlElement(name=COLUMN_QUANTITY, type=int.class, required=true)
    private int quantity;

    public Ticket() {
    }

    public Ticket(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.quantity;
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
        if (this.quantity != other.quantity) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Ticket ID=\"").append(ID).append("\">");
        builder.append("<Name>").append(name != null ? name : "NULl").append("</Name>");
        builder.append("<Quantity>").append(quantity).append("</Quantity>");
        
        builder.append("</Ticket>");
        
        return builder.toString();
    }
}
