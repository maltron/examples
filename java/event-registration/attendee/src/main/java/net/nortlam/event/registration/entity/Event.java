package net.nortlam.event.registration.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

//@Entity(name="Event")
//@Table(name="EVENT", uniqueConstraints = 
//        @UniqueConstraint(name = "EVENT_DESIGNATION_EDITION",
//                                columnNames = {"EDITION", "DESIGNATION"}))
@XmlRootElement(name="Event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Serializable {

    public static final String COLUMN_ID = "id";
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="EVENT_ID")
    @XmlAttribute(name = COLUMN_ID, required=false)
    private long ID;
    
    public static final String COLUMN_EDITION = "edition";
//    @Column(name="EDITION", columnDefinition = "INT", nullable = false)
    @XmlElement(name=COLUMN_EDITION, type=int.class, required=true)
    private int edition;
    
    public static final int LENGTH_DESIGNATION = 50;
    public static final String COLUMN_DESIGNATION = "designation";
//    @Column(name="DESIGNATION", length = LENGTH_DESIGNATION, nullable = false)
    @XmlElement(name=COLUMN_DESIGNATION, type=String.class, required=true)
    private String designation;
    
    public static final String COLUMN_TITLE = "title";
    public static final int LENGTH_TITLE = 120;
//    @Column(name="TITLE", length = LENGTH_TITLE, nullable = false)
    @XmlElement(name=COLUMN_TITLE, type=String.class, required=true)
    private String title;
    
    // Location
    public static final String COLUMN_LOCATION = "location";
    public static final int LENGTH_LOCATION = 90;
//    @Column(name="LOCATION", length=LENGTH_LOCATION, nullable=true)
    @XmlElement(name=COLUMN_LOCATION, type=String.class, required=false)
    private String location;
    
    public static final String COLUMN_ADDRESS = "address";
    public static final int LENGTH_ADDRESS = 150;
//    @Column(name="ADDRESS", length = LENGTH_ADDRESS, nullable = true)
    @XmlElement(name=COLUMN_ADDRESS, type=String.class, required=false)
    private String address;
    
    public static final int LENGTH_CITY = 100;
    public static final String COLUMN_CITY = "city";
//    @Column(name="CITY", length = LENGTH_CITY, nullable = true)
    @XmlElement(name=COLUMN_CITY, type=String.class, required=false)
    private String city;
    
    public static final int LENGTH_REGION = 70;
    public static final String COLUMN_REGION = "region";
//    @Column(name="REGION", length = LENGTH_REGION, nullable = true)
    @XmlElement(name=COLUMN_REGION, type=String.class, required=false)
    private String region;
    
    public static final int LENGTH_ZIP_CODE = 15;
    public static final String COLUMN_ZIP_CODE = "zipCode";
//    @Column(name="ZIP_CODE", length = LENGTH_ZIP_CODE, nullable = true)
    @XmlElement(name=COLUMN_ZIP_CODE, type=String.class, required=false)
    private String zipCode;
    
    public static final int LENGTH_COUNTRY = 100;
    public static final String COLUMN_COUNTRY = "country";
//    @Column(name="COUNTRY", length = LENGTH_COUNTRY, nullable = true)
    @XmlElement(name=COLUMN_COUNTRY, type=String.class, required=false)
    private String country;
    
    // Starts and Ends
//    @Temporal(TemporalType.TIMESTAMP)
    public static final String COLUMN_EVENT_STARTS = "starts";
//    @Column(name="EVENT_STARTS", nullable = false)
    @XmlElement(name=COLUMN_EVENT_STARTS, type=Date.class, required=true)
    private Date starts;
    
    public static final String COLUMN_EVENT_ENDS = "ends";
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name="EVENT_ENDS", nullable = false)
    @XmlElement(name=COLUMN_EVENT_ENDS, type=Date.class, required=true)
    private Date ends;
    
    public static final int LENGTH_DESCRIPTION = 255;
    public static final String COLUMN_DESCRIPTION = "description";
//    @Column(name="DESCRIPTION", length = LENGTH_DESCRIPTION, nullable = true)
    @XmlElement(name=COLUMN_DESCRIPTION, type=String.class, required=false)
    private String description;
    
    public static final String COLUMN_ORGANIZER = "organizer";
//    @Column(name="ORGANIZER", nullable = false)
    @XmlElement(name=COLUMN_ORGANIZER, type=long.class, required=true)
    private long organizer;
    
    public static final String COLUMN_TICKETS = "tickets";
//    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name="EVENT_SELLS_TICKETS",
//            joinColumns=@JoinColumn(name="EVENT_ID", referencedColumnName="EVENT_ID"),
//            inverseJoinColumns=@JoinColumn(name="TICKET_ID", referencedColumnName="TICKET_ID"))
    @XmlElements({@XmlElement(name = COLUMN_TICKETS, type = Ticket.class, required = false)})
    private Set<Ticket> tickets;
    
    public Event() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getStarts() {
        return starts;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOrganizer() {
        return organizer;
    }

    public void setOrganizer(long organizer) {
        this.organizer = organizer;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 37 * hash + this.edition;
        hash = 37 * hash + Objects.hashCode(this.designation);
        hash = 37 * hash + Objects.hashCode(this.title);
        hash = 37 * hash + Objects.hashCode(this.location);
        hash = 37 * hash + Objects.hashCode(this.address);
        hash = 37 * hash + Objects.hashCode(this.city);
        hash = 37 * hash + Objects.hashCode(this.region);
        hash = 37 * hash + Objects.hashCode(this.zipCode);
        hash = 37 * hash + Objects.hashCode(this.country);
        hash = 37 * hash + Objects.hashCode(this.starts);
        hash = 37 * hash + Objects.hashCode(this.ends);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + (int) (this.organizer ^ (this.organizer >>> 32));
        hash = 37 * hash + Objects.hashCode(this.tickets);
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
        final Event other = (Event) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.edition != other.edition) {
            return false;
        }
        if (!Objects.equals(this.designation, other.designation)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.region, other.region)) {
            return false;
        }
        if (!Objects.equals(this.zipCode, other.zipCode)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.starts, other.starts)) {
            return false;
        }
        if (!Objects.equals(this.ends, other.ends)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (this.organizer != other.organizer) {
            return false;
        }
        if (!Objects.equals(this.tickets, other.tickets)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<Event ID=\"").append(ID).append("\">");
        builder.append("<Designation>").append(designation != null ? designation : null).append("</Designation>");
        builder.append("<Edition>").append(edition).append("</Edition>");
        builder.append("<Title>").append(title != null ? title : "NULL").append("</Title>");
        builder.append("<Location>").append(location != null ? location : "NULL").append("</Location>");
        builder.append("<Address>").append(address != null ? address : "NULL").append("</Address>");
        builder.append("<City>").append(city != null ? city : "NULL").append("</City>");
        builder.append("<Region>").append(region != null ? region : "NULL").append("</Region>");
        builder.append("<ZipCode>").append(zipCode != null ? zipCode : "NULL").append("</ZipCode>");
        builder.append("<Country>").append(country != null ? country : "NULL").append("</Country>");
        builder.append("<EventStarts>").append(starts != null ? starts : "NULL").append("</EventStarts>");
        builder.append("<EventEnds>").append(ends != null ? ends : "NULL").append("</EventEnds>");
        builder.append("<Description>").append(description != null ? description : "NULL").append("</Description>");
        builder.append("<Organizer>").append(organizer).append("</Organizer>");
        builder.append("<Tickets>").append(tickets != null ? tickets.toString() : "NULL").append("</Tickets>");
        builder.append("</Event>");
        
        return builder.toString();
    }
}
