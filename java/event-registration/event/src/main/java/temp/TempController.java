package temp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;
import static net.nortlam.event.registration.util.Extraction.extractDesignation;
import static net.nortlam.event.registration.util.Extraction.extractEdition;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name = "temp")
@RequestScoped
public class TempController extends EventRegistrationCommonController
        implements Serializable {

    private String selectedCity;
    private List<String> cities;
    private Set<Ticket> ticketSelected;

    private static final int[] VALUES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Event event;
    
    @EJB
    private Service service;

    private static final Logger LOG = Logger.getLogger(TempController.class.getName());

    public TempController() {
    }

    public int[] getValue() {
        return VALUES;
    }
    
    public void setEventID(String eventID) {
        try {
            event = service.findByID(Long.parseLong(eventID));
            
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getEventID() {return null;}
    
    public void setDesignationEdition(String designationEdition) {
        String designation = extractDesignation(designationEdition);
        int edition = extractEdition(designationEdition);
        
        if(designation == null || edition == 0) {
            LOG.log(Level.WARNING, "### setDesignationEdition() Arguments *NOT* valid:"+
                    " Designation:{0} Edition:{1}", new Object[] {designation, edition});
            redirectNotFoundError();
            return;
        }
        
        try {
            event = service.findByDesignationEdition(designation, edition);
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getDesignationEdition() { return null; } // NOT USED
    
    

    public Event getEvent() {
        if (event == null) {
            event = new Event();
            event.setTitle("TITLE");
            
            Ticket ONE = new Ticket("ONE", 200);
            Ticket TWO = new Ticket("TWO", 300);
            Set<Ticket> tickets = new HashSet<Ticket>(2);
            tickets.add(ONE); tickets.add(TWO);
            event.setTickets(tickets);

            for (Ticket t : event.getTickets()) {
                LOG.log(Level.INFO, ">>> getEvent() Ticket:{0}", t);
            }
        }

        return event;
    }
    
    public Set<Ticket> getTicketSelected() {
        return ticketSelected;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<String> getCities() {
        return cities;
    }

    @PostConstruct
    private void init() {
        cities = new ArrayList<String>();
        cities.add("Miami");
        cities.add("London");
        cities.add("Paris");
        cities.add("Istanbul");
        cities.add("Berlin");
        cities.add("Barcelona");
        cities.add("Rome");
        cities.add("Brasilia");
        cities.add("Amsterdam");
    }

    public void save(ActionEvent e) {
        info("Selected City:", getSelectedCity());
    }

    public String listTickets() {
        for (Ticket ticket : event.getTickets()) {
            LOG.log(Level.INFO, "Ticket:{0}", ticket);
            if (ticket.getQuantitySelected() > 0) {
                if(ticketSelected == null) ticketSelected = new HashSet<Ticket>();
                ticketSelected.add(ticket);
            }
        }

        if (ticketSelected == null) {
            error("No tickets were selected");
            return null;
        }
        
        return "ticket2";
    }
}
