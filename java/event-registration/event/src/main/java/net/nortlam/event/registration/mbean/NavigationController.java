package net.nortlam.event.registration.mbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import net.nortlam.event.registration.util.EventRegistrationCommonController;

/**
 * Please check the file pretty-config.xml in order to match all the links in 
 * here
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name = "nav")
@ViewScoped
public class NavigationController extends EventRegistrationCommonController 
                                                    implements Serializable {

    public void goNewEvent() {
        redirect(hostOrganizerService(), "event/new");
    }
    
    public void goOrganizer() {
        redirect(hostOrganizerService(), "event/all");
    }
}
