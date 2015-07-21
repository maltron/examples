package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.nortlam.event.registration.util.Encrypt;

@Entity(name="Organizer")
@Table(name="ORGANIZER", uniqueConstraints = {@UniqueConstraint(
                                    columnNames = {"FIRST_NAME", "LAST_NAME"}),
                                @UniqueConstraint(columnNames = "EMAIL")})
@XmlRootElement(name="Organizer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Organizer implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORGANIZER_ID")
    @XmlAttribute(name = "id", required = false)
    private long ID;

    public static final int LENGTH_FIRSTNAME = 50;
    @Column(name="FIRST_NAME", length = LENGTH_FIRSTNAME, nullable = false)
    @XmlElement(name="firstName", type=String.class, required=true)
    private String firstName;
    
    public static final int LENGTH_LASTNAME = 50;
    @Column(name="LAST_NAME", length = LENGTH_LASTNAME, nullable = false)
    @XmlElement(name="lastName", type=String.class, required=true)
    private String lastName;
    
    public static final int LENGTH_EMAIL = 70;
    @Column(name="EMAIL", length = LENGTH_EMAIL, nullable = false)
    @XmlElement(name="email", type=String.class, required=true)
    private String email;
    
    public static final int LENGTH_PASSWORD = 120;
    @Column(name="PASSWORD", length = LENGTH_PASSWORD, nullable = false)
    @XmlElement(name="password", type=String.class, required=true)
    private String password;

    public Organizer() {
    }

    public Organizer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Encrypt.encrypt(password); // ENCRYPTION GOES HERE
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 59 * hash + Objects.hashCode(this.firstName);
        hash = 59 * hash + Objects.hashCode(this.lastName);
        hash = 59 * hash + Objects.hashCode(this.email);
        hash = 59 * hash + Objects.hashCode(this.password);
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
        final Organizer other = (Organizer) obj;
        if (this.ID != other.ID) {
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
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Organizer ID=\"").append(ID).append("\">");
        builder.append("<FirstName>").append(firstName != null ? firstName : "NULL").append("</FirstName>");
        builder.append("<LastName>").append(lastName != null ? lastName : "NULL").append("</LastName>");
        builder.append("<Email>").append(email != null ? email : "NULL").append("</Email>");
        builder.append("<Password>").append(password != null ? "*****" : "NULL").append("</Password>");
        builder.append("</Organizer>");
        
        return builder.toString();
    }
}
