package com.websocket.poc;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.log4j.Logger;

/*
 * Route gets a message from direct endpoint to a websocket. The MDB sends messages to the direct endpoint.
 */

@Startup
@ApplicationScoped
@ContextName("camel-websocket")
public class RouteQueueToWebsocket extends RouteBuilder {

	Logger logger = Logger.getLogger(RouteQueueToWebsocket.class.getName());
	
    @Override
    public void configure() throws Exception {
    	
    	//experiment using both types of endpoint types
        from("direct:consumer")
            .routeId("Route2-Queue-Websocket")
            .log("Message: ${body}")
            //.to("ahc-ws://127.0.0.1:8080/websocket-camel-poc/websocket");
            .to("websocket://localhost:9090/camel-ws?sendToAll=true");
    }

}