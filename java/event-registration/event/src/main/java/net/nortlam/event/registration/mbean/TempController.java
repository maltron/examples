package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.entity.Ticket;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

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

    private static final Logger LOG = Logger.getLogger(TempController.class.getName());

    public TempController() {
    }

    public int[] getValue() {
        return VALUES;
    }

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
//            info("Successfully found some tickets selected");
//        } else {
            error("No tickets were selected");
            return null;
        }
        
        return "result_datatable_tickets";
    }
}
