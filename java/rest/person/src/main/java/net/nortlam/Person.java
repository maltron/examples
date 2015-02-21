package net.nortlam;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mauricio "Maltron" Leal */
@Table(name="TABLE_PERSON", uniqueConstraints = 
        {@UniqueConstraint(columnNames = {"FIRST_NAME", "LAST_NAME"})})
@Entity(name = "Person")
@NamedQueries({
    @NamedQuery(name=Person.FIND_BY_FIRST_LAST_NAME, 
            query = "SELECT person FROM Person person WHERE person.firstName=:FIRST_NAME and person.lastName=:LAST_NAME")
})
@XmlRootElement(name="Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(Person.class.getName());
    
    public static final String FIND_BY_FIRST_LAST_NAME = "Person.findByFirstLastName()";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    @XmlAttribute(name = "ID", required=false)
    private long ID;
    
    @Column(name="FIRST_NAME", length = 30, nullable = false)
    @XmlElement(name="firstName", type = String.class, required=true)
    private String firstName;
    
    @Column(name="LAST_NAME", length = 30, nullable = false)
    @XmlElement(name="lastName", type=String.class, required=true)
    private String lastName;
    
    @Column(name="STATUS")
    @XmlElement(name="status", type=Boolean.class, required=false)
    private Boolean status;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
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

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 19 * hash + Objects.hashCode(this.firstName);
        hash = 19 * hash + Objects.hashCode(this.lastName);
        hash = 19 * hash + Objects.hashCode(this.status);
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
        final Person other = (Person) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Person ID=\"").append(ID).append("\">");
        builder.append("<FirstName>").append(firstName).append("</FirstName>");
        builder.append("<LastName>").append(lastName).append("</LastName>");
        builder.append("<Status>").append(status != null ? status.toString() : "NULL").append("</Status>");
        builder.append("</Person>");
        
        return builder.toString();
    }
}
