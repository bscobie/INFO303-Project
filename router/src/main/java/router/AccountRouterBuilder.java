package router;

import domain.Account;
import domain.Customer;
import domain.DomainConverter;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *
 * @author benscobie
 */
public class AccountRouterBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // create HTTP endpoint for receiving messages via HTTP
        from("jetty:http://localhost:8080/api/account?enableCORS=true")
        // make message in-only so web browser doesn't have to wait on a non-existent response
        .setExchangePattern(ExchangePattern.InOnly)
        .convertBodyTo(String.class)
        .log("${body}")
        .to("jms:queue:new-accounts-json");
        
        from("jms:queue:new-accounts-json") 
        .unmarshal().json(JsonLibrary.Gson, Account.class) 
        .to("jms:queue:new-accounts");
        
        from("jms:queue:new-accounts")
        .bean(DomainConverter.class, "accountToCustomer(${body})")
        .to("jms:queue:new-customers");
 
        from("jms:queue:new-customers")
        // remove headers so they don't get sent to Vend
        .removeHeaders("*")
        // add authentication token to authorization header
        .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
        // marshal to JSON
        .marshal().json(JsonLibrary.Gson)  // only necessary if the message is an object, not JSON
        .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
        // set HTTP method
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        // send it
        .to("https://info303otago.vendhq.com/api/2.0/customers?throwExceptionOnFailure=false")
        // handle response
        .choice()
            .when().simple("${header.CamelHttpResponseCode} == '201'")  // change to 200 for PUT
                .convertBodyTo(String.class)
                .to("jms:queue:vend-response")
            .otherwise()
                .log("ERROR RESPONSE ${header.CamelHttpResponseCode} ${body}")
                .convertBodyTo(String.class)
                .to("jms:queue:vend-error")
        .endChoice();
        
        from("jms:queue:vend-response") 
        .setBody().jsonpath("$.data")
        .marshal().json(JsonLibrary.Gson)
        .to("jms:queue:extracted-vend-json"); 
        
        from("jms:queue:extracted-vend-json") 
        .unmarshal().json(JsonLibrary.Gson, Customer.class) 
        .to("jms:queue:extracted-customer");
        
        from("jms:queue:extracted-customer")
	.toD("graphql://http://localhost:8082/graphql?query=mutation{addAccount(account: {id:\"${body.id}\", email:\"${body.email}\", username:\"${body.customerCode}\", firstName:\"${body.firstName}\", lastName:\"${body.lastName}\", group:\"${body.group}\"}) { id email username firstName lastName group}}")
	.log("GraphQL service called");
        
        
    }
    
}
