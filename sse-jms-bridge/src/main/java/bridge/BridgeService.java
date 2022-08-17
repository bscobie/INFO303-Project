package bridge;

import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark George
 */
public class BridgeService {

	private final static String USERNAME = "scobe266";

	private final static String JMS_QUEUE = "new-sale";
	private final static String SSE_EVENT = "new-sale";

	private final static String URL = "http://isgb.otago.ac.nz/vend/sales/stream/" + USERNAME;

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(BridgeService.class);
		try {
			System.out.println("\nStarting SSE/JMS Bridge.");
			JMSSender sender = new JMSSender(JMS_QUEUE);
			SSEReceiver receiver = new SSEReceiver(URL, SSE_EVENT, sender);
			receiver.start();
			Thread.currentThread().join();
		} catch (InterruptedException ex) {
			// nobody cares
		} catch (URISyntaxException ex) {
			log.error("SSE Service URI is not valid", ex);
		}
	}

}
