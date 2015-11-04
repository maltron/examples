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

/**
 * This is the events the Attendee is talking part of 
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Entity(name="Enroll")
@Table(name="ENROLL")
@XmlRootElement(name="Enroll")
@XmlAccessorType(XmlAccessType.FIELD)
public class Enroll implements Serializable {
    
    public static final String COLUMN_ID = "id";
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ENROLL_ID")
    @XmlAttribute(name = COLUMN_ID, required=false)
    private long ID;

    public static final String COLUMN_ATTENDEE = "attendeeID";
    @Column(name="ATTENDEE_ID", nullable = false)
    @XmlElement(name="attendeeID", type=long.class, required=true)
    private long attendeeID;

    @Column(name="EVENT_ID", nullable = false)
    @XmlElement(name="eventID", type=long.class, required=true)
    private long eventID;
    
    @Column(name="TITLE", length = Event.LENGTH_TITLE, nullable = true)
    @XmlElement(name="title", type=String.class, required=false)
    private String title;
    
    public Enroll() {
    }
    
    public Enroll(Event event, Attendee attendee) {
        setAttendeeID(attendee.getID());
        setEventID(event.getID());
        setTitle(event.getTitle());
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getAttendeeID() {
        return attendeeID;
    }

    public void setAttendeeID(long attendeeID) {
        this.attendeeID = attendeeID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 37 * hash + (int) (this.attendeeID ^ (this.attendeeID >>> 32));
        hash = 37 * hash + (int) (this.eventID ^ (this.eventID >>> 32));
        hash = 37 * hash + Objects.hashCode(this.title);
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
        final Enroll other = (Enroll) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.attendeeID != other.attendeeID) {
            return false;
        }
        if (this.eventID != other.eventID) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Enroll ID=\"").append(ID).append("\">");
        builder.append("<Attendee ID=\"").append(attendeeID > 0 ? attendeeID : "ZERO")
                .append("\"/>");
        builder.append("<Event ID=\"").append(eventID > 0 ? eventID : "ZERO")
                .append("\"/>");
        builder.append("<Title>").append(title != null ? title : "NUL")
                .append("</Title>");
        builder.append("</Enroll>");
        
        return builder.toString();
    }
}
