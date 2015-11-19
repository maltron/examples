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
    
    public static final String TOPIC_REFUND_INFORMATION = "java:/topic/notification/order/refund";
    public static final String TOPIC_REFUND = "activemq/topic/notification/order/refund";
    
    public Connection connection();
    public Topic topicOrder();
    public Topic topicOrderRefund();

}
