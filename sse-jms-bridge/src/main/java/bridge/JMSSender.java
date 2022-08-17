package bridge;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark George
 */
public class JMSSender {

	private Logger log = LoggerFactory.getLogger(JMSSender.class);
	private Session session = null;
	private MessageProducer producer = null;

	public JMSSender(String queue) {
		try {

			Connection connection = new ActiveMQConnectionFactory().createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queue);
			producer = session.createProducer(destination);
			connection.start();

		} catch (JMSException ex) {
			log.error("Error creating JMS connection - is ActiveMQ running?", ex);
		}
	}

	public void sendMessage(String msg) {
		try {
			TextMessage message = session.createTextMessage();
			message.setText(msg);
			producer.send(message);
		} catch (JMSException ex) {
			log.error("Error sending message", ex);
		}

	}

}
