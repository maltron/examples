package net.nortlam.event.registration.util;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@ManagedBean(name = "format")
@ViewScoped
public class FormatController extends EventRegistrationCommonController 
                                                        implements Serializable {
    
    public String today() {
        return formatDate(new Date());
    }
    
    public String formatDate(Date value) {
        return DATE_FORMAT.format(value);
    }
    
    public String formatDate(Date starts, Date ends) {
        StringBuilder builder = new StringBuilder();
        // Starts
        builder.append(DATE_FORMAT_STARTS.format(starts));
        // Ends
        builder.append(DATE_FORMAT_ENDS.format(ends));
        
        return builder.toString();
    }
    
}
