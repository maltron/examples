package temp;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
@ManagedBean(name="HHH")
@RequestScoped
public class HandlerController extends EventRegistrationCommonController
                                                        implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HandlerController.class.getName());
    
    @ManagedProperty(value="testevent")
    private EventMBean mbean;

    @EJB
    private Service service;

    public HandlerController() {
    }
    
    
    public void setMbean(EventMBean mbean) {
        this.mbean = mbean;
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
            Event event = service.findByDesignationEdition(designation, edition);
            mbean.setEvent(event);
            
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getDesignationEdition() { return null; } // NOT USED

    public Event getEvent() {
        return mbean.getEvent();
    }
    
}
