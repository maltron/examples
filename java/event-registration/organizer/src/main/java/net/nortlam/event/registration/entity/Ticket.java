package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.util.Objects;
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
    
    public static final String COLUMN_QUANTITY_AVAILABLE = "quantityAvailable";
//    @Column(name="QUANTITY_AVAILABLE", columnDefinition = "INT", nullable = false)
    @XmlElement(name=COLUMN_QUANTITY_AVAILABLE, type=int.class, required=true)
    private int quantityAvailable;
    
    public static final String COLUMN_QUANTITY_SELECTED = "quantitySelected";
//    @Transient
//    @Column(name="QUANTITY_SELECTED", columnDefinition = "INT", nullable = true)
    @XmlElement(name=COLUMN_QUANTITY_SELECTED, type=int.class, required=false)
    private int quantitySelected;

    public Ticket() {
    }

    public Ticket(String name, int quantityAvailable) {
        this.name = name;
        this.quantityAvailable = quantityAvailable;
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
        StringBuilder builder = new StringBuilder();
        builder.append("<Ticket ID=\"").append(ID).append("\">");
        builder.append("<Name>").append(name != null ? name : "NULl").append("</Name>");
        builder.append("<QuantityAvailable>").append(quantityAvailable > 0 ? 
                quantityAvailable : "ZERO")
                .append("</QuantityAvailable>");
        builder.append("<QuantitySelected>").append(quantitySelected > 0 ?
                quantitySelected : "ZERO")
                .append("</QuantitySelected>");
        
        builder.append("</Ticket>");
        
        return builder.toString();
    }
}
