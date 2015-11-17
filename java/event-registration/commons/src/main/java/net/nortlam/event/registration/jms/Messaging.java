package net.nortlam.event.registration.jms;

import javax.jms.Connection;
import javax.jms.Topic;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public interface Messaging {

    public static final String FACTORY_INFORMATION = "java:/AMQConnectionFactory";
    public static final String TOPIC_ORDER_INFORMATION = "java:/topic/notification/order";
    public static final String TOPIC_ORDER = "activemq/topic/notification/order";
    
    public Connection connection();
    public Topic topicOrder();

}
