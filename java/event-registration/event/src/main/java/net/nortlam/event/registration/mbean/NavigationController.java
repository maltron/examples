package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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

    public void goNewEvent() {
        redirect(hostOrganizerService(), "event/new");
    }
    
    public void goOrganizer() {
        redirect(hostOrganizerService(), "event/all");
    }
    
    public void goRegister() {
        redirect(hostEventService(), String.format("register/%d"));
    }
    
    public void goEventHome() {
        redirect(hostEventService(), "");
    }
}
