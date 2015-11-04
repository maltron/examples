package temp;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;
import static net.nortlam.event.registration.util.Extraction.extractDesignation;
import static net.nortlam.event.registration.util.Extraction.extractEdition;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name="e1")
//@ViewScoped
@RequestScoped
public class Event1Controller extends EventRegistrationCommonController
                                                        implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(Event1Controller.class.getName());

    @EJB
    private Service service;
    private Event event;
    
    public Event1Controller() {
    }
    
    public void setDesignationEdition(String designationEdition) {
        LOG.log(Level.INFO, ">>> setDesignationEdition({0})", designationEdition);
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
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public void show(ActionEvent event) {
        info("Event is", event != null ? "NOT NULL" : "NULL");
    }
    

}
