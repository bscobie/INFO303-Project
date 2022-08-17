package router;

import domain.CustomerCreator;
import domain.Sale;
import domain.Summary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *
 * @author benscobie
 */
public class SalesRouterBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {       
        from("jms:queue:new-sale")
        .convertBodyTo(String.class)
        //.log("${body}")
        .to("jms:queue:new-vend-sale");
        
        from("jms:queue:new-vend-sale")
        .unmarshal().json(JsonLibrary.Gson, Sale.class) 
        //.log("${body}")
        .to("jms:queue:extracted-sale");
        
        from("jms:queue:extracted-sale")
        .setProperty("group").simple("${body.customer.group}")
        .setProperty("id").simple("${body.customer.id}")
        .setProperty("firstName").simple("${body.customer.firstName}")
        .setProperty("lastName").simple("${body.customer.lastName}")
        .setProperty("email").simple("${body.customer.email}")
        .to("jms:queue:sale-body");
        
        from("jms:queue:sale-body")
        .removeHeaders("*")
        .marshal().json(JsonLibrary.Gson)
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .toD("http://localhost:8083/api/sales")
        .to("jms:queue:post-sale");
        
        from("jms:queue:post-sale")
        .removeHeaders("*") // remove headers to stop them being sent to the service
        .setBody().constant(null) // some services throw a 400 error if you send a body in a GET, so remove the body
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        //.log("${exchangeProperty.id}")
        .toD("http://localhost:8083/api/sales/customer/${exchangeProperty.id}/summary")
        .to("jms:queue:customer-summary");
        
        from("jms:queue:customer-summary")
        .unmarshal().json(JsonLibrary.Gson, Summary.class) 
        .setProperty("calculatedGroup").simple("${body.group}")
        .to("jms:queue:extracted-group");
        
        from("jms:queue:extracted-group")
        .setProperty("newGroup").method(GroupUpdater.class,"updateGroup(${exchangeProperty.calculatedGroup}, ${exchangeProperty.group})")
        .toD("graphql://http://localhost:8082/graphql?query=mutation{changeGroup(id:\"${exchangeProperty.id}\", newGroup:\"${exchangeProperty.newGroup}\") { id email username firstName lastName group}}")
	.log("GraphQL service called")
        .to("jms:queue:updated-customer");
	
        from("jms:queue:updated-customer") 
        .setBody().jsonpath("$.data")
        .marshal().json(JsonLibrary.Gson)
        .bean(CustomerCreator.class, "createCustomer(${exchangeProperty.id},${exchangeProperty.newGroup},${exchangeProperty.email},${exchangeProperty.firstName},${exchangeProperty.lastName},${exchangeProperty.username})")     
        .removeHeaders("*")
        .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
        .marshal().json(JsonLibrary.Gson)  // only necessary if the message is an object, not JSON
        .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
        .setHeader(Exchange.CONTENT_TYPE).constant("application/json") 
        .toD("https://info303otago.vendhq.com/api/2.0/customers/${exchangeProperty.id}")
        .choice()
            .when().simple("${header.CamelHttpResponseCode} == '200'")  // change to 200 for PUT
            .convertBodyTo(String.class)
            .to("jms:queue:update-vend-response")
        .otherwise()
            .log("ERROR RESPONSE ${header.CamelHttpResponseCode} ${body}")
            .convertBodyTo(String.class)
            .to("jms:queue:update-vend-error")
        .endChoice();
    }  
}