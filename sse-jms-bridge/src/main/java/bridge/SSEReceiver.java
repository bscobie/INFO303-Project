package bridge;


import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark George
 */
public class SSEReceiver {

	private final static Logger log = LoggerFactory.getLogger(SSEReceiver.class);

	private final EventHandler handler;
	private final String uri;

	public SSEReceiver(String uri, String event, JMSSender sender) {
		this.uri = uri;

		handler = new EventHandler() {
			@Override
			public void onOpen() throws Exception {
				log.info("SSE connection opened.");
			}

			@Override
			public void onClosed() throws Exception {
				log.info("SSE connection closed.");
			}

			@Override
			public void onMessage(String evt, MessageEvent msg) throws Exception {
				if (evt.equals(event)) {
					log.info("New message: {}", msg.getData());
					sender.sendMessage(msg.getData());
				}
			}

			@Override
			public void onComment(String comment) throws Exception {
			}

			@Override
			public void onError(Throwable err) {
				log.error("Error", err);
			}
		};

	}

	public void start() throws URISyntaxException  {
		EventSource es = new EventSource.Builder(handler, new URI(uri)).build();
		es.start();
	}

}
