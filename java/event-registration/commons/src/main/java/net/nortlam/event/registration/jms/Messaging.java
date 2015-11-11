package net.nortlam.event.registration.jms;

import javax.jms.Connection;
import javax.jms.Topic;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public interface Messaging {

    public static final String FACTORY_INFORMATION = "java:/AMQConnectionFactory";
    public static final String TOPIC_INFORMATION = "java:/topic/event-registration";
    public static final String TOPIC = "activemq/topic/event-registration";
    
    public Connection connection();
    public Topic topic();

}
