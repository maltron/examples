package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

@ManagedBean(name="organizer")
@ViewScoped
public class OrganizerController extends EventRegistrationCommonController 
                                                    implements Serializable {

    private static final Logger LOG = Logger.getLogger(
                                        OrganizerController.class.getName());
    @EJB
    private Service service;
    
    private boolean isNew;
    private Organizer organizer;

    public OrganizerController() {}
    
    public void setOrganizerID(String organizerID) {
        try {
            long value = Long.parseLong(organizerID);
            organizer = service.read(value);
            isNew = false;
            
        } catch(NumberFormatException ex) {
            LOG.log(Level.WARNING, "### setOrganizerID() NUMBER FORMAT EXCEPTION:",
                                                                ex.getMessage());
            redirectInternalServerError();
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public String getOrganizerID() {return null;} // NOTHING TO DO
    
    public Organizer getOrganizer() {
        if(organizer == null) {
            isNew = true;
            organizer = new Organizer();
        }
        
        return organizer;
    }
    
    public void save(ActionEvent event) {
        LOG.log(Level.INFO, ">>> save() isNew ? {0} Organizer ID:{1}", new Object[] {
            isNew, organizer.getID()
        });
        try {
            if(isNew) organizer = service.create(organizer);
            else organizer = service.update(organizer);
            
            isNew = false;
            info("Successfull", "Organizer information saved");
            
        } catch(AlreadyExistsException ex) {
            error("Already Exists", ex.getMessage());
        } catch(BiggerException | MissingInformationException ex) {
            error("Illegal Information", ex.getMessage());
        } catch(IllegalArgumentException ex) {
            error("Illegal Argument", ex.getMessage());
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
}
