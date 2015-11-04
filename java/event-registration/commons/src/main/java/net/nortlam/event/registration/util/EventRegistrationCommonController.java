package net.nortlam.event.registration.util;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class EventRegistrationCommonController extends AbstractController {
    
    private static final Logger LOG = Logger.getLogger(EventRegistrationCommonController.class.getName());
    
    private static final String ERROR_NOT_FOUND = "error/notfound";
    private static final String ERROR_INTERNAL_SERVER = "error/internalserver";
    
    public static final SimpleDateFormat DATE_FORMAT = 
                                new SimpleDateFormat("EEEE,   MMM d, yyyy 'at' HH:mm (z)");
    
    public static final SimpleDateFormat DATE_FORMAT_STARTS = 
                        new SimpleDateFormat("EEEE, MMM d, yyyy 'from' h:mma");
    public static final SimpleDateFormat DATE_FORMAT_ENDS = 
                                        new SimpleDateFormat(" 'to' h:mma (z)");

    public static final String PARAMETER_EVENT_SERVICE_HOST = "service-event";
    public static final String DEFAULT_EVENT_SERVICE_HOST = "http://localhost:8080/event";
    
    public static final String PARAMETER_ATTENDEE_SERVICE_HOST = "service-attendee";
    public static final String DEFAULT_ATTENDEE_SERVICE_HOST = "http://localhost:8080/attendee";
    
    public static final String PARAMETER_ORGANIZER_SERVICE_HOST = "service-organizer";
    public static final String DEFAULT_ORGANIZER_SERVICE_HOST = "http://localhost:8080/organizer";
    
    public static final String KEY_EVENT = "KEY_EVENT";
    
    public void redirectNotFoundError() {
        redirect(ERROR_NOT_FOUND);
    }
    
    public void redirectInternalServerError() {
        redirect(ERROR_INTERNAL_SERVER);
    }
    
    protected String hostAttendeeService() {
        String value = getExternal().getInitParameter(PARAMETER_ATTENDEE_SERVICE_HOST);
        return value != null ? value : DEFAULT_ATTENDEE_SERVICE_HOST;
    }
    
    protected String hostOrganizerService() {
        String value = getExternal().getInitParameter(PARAMETER_ORGANIZER_SERVICE_HOST);
        return value != null ? value : DEFAULT_ORGANIZER_SERVICE_HOST;
    }
    
    protected String hostEventService() {
        String value = getExternal().getInitParameter(PARAMETER_EVENT_SERVICE_HOST);
        return value != null ? value : DEFAULT_EVENT_SERVICE_HOST;
    }
    
}