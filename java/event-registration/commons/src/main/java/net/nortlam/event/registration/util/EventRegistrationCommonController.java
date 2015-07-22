package net.nortlam.event.registration.util;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class EventRegistrationCommonController extends AbstractController {
    
    private static final Logger LOG = Logger.getLogger(EventRegistrationCommonController.class.getName());
    
    private static final String ERROR_NOT_FOUND = "error/notfound";
    private static final String ERROR_INTERNAL_SERVER = "error/internalserver";
    
    public static final SimpleDateFormat DATE_FORMAT_STARTS = 
                        new SimpleDateFormat("EEEE, MMM d, yyyy 'from' h:mma");
    public static final SimpleDateFormat DATE_FORMAT_ENDS = 
                                        new SimpleDateFormat(" 'to' h:mma (z)");

    public static final String PARAMETER_EVENT_SERVICE_HOST = "service-event";
    public static final String DEFAULT_EVENT_SERVICE_HOST = "http://localhost:8080/event";
    
    public static final String PARAMETER_USER_SERVICE_HOST = "service-user";
    public static final String DEFAULT_USER_SERVICE_HOST = "http://localhost:8080/user";
    
    public void redirectNotFoundError() {
        redirect(ERROR_NOT_FOUND);
    }
    
    public void redirectInternalServerError() {
        redirect(ERROR_INTERNAL_SERVER);
    }
    
    protected String hostUserService() {
        String value = getExternal().getInitParameter(PARAMETER_USER_SERVICE_HOST);
        return value != null ? value : DEFAULT_USER_SERVICE_HOST;
    }
    
    protected String hostEventService() {
        String value = getExternal().getInitParameter(PARAMETER_EVENT_SERVICE_HOST);
        return value != null ? value : DEFAULT_EVENT_SERVICE_HOST;
    }
}
