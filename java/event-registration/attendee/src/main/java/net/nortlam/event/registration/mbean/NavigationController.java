package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.nortlam.event.registration.entity.Attendee;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

/**
 * Please check the file pretty-config.xml in order to match all the links in 
 * here
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Named("nav")
@ViewScoped
public class NavigationController extends EventRegistrationCommonController 
                                                    implements Serializable {
    @EJB
    private Service service;
    
    public void goEventNew() {
        redirect("event/new");
    }
    
    public void goEventEdit(Event event) {
        redirect(String.format("event/%d", event.getID()));
    }
    
    public void goEventAll() {
        redirect("event/all");
    }
    
    public void goAttendeeEdit() {
        try {
            String email = getExternal().getRemoteUser();
            Attendee attendee = service.findByEmail(email);
            redirect(String.format("%d", attendee.getID()));
            
        } catch(NotFoundException ex) {
            redirectNotFoundError();
        } catch(InternalServerErrorException ex) {
            redirectInternalServerError();
        }
    }
    
    public void goAttendeeNew() {
        redirect("new");
    }
}
