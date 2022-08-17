package router;

import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Router {

	public static void main(String[] args) throws Exception {
            // create default context
            CamelContext camel = new DefaultCamelContext();
            // register ActiveMQ as the JMS handler
            ActiveMQComponent activemq = ActiveMQComponent.activeMQComponent();
            camel.addComponent("jms", activemq);
            // transfer the entire exchange, or just the body and headers?
            activemq.setTransferExchange(true);
            // trust all classes being used to send serialised domain objects
            activemq.setTrustAllPackages(true);
            // turn exchange tracing on or off (false is off)
            camel.setTracing(false);
            // enable stream caching so that things like loggers don't consume the messages
            camel.setStreamCaching(true);
            
            // create and add the builder(s)
            camel.addRoutes(new AccountRouterBuilder());
            camel.addRoutes(new SalesRouterBuilder());

            // start routing
            System.out.println("Starting router...");
            camel.start();
            System.out.println("... ready.  Press enter to shutdown.");
            System.in.read();
            camel.stop();
	}

}
