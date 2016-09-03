package com.websocket.poc;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.log4j.Logger;

/*
 * Route uses a timer to create a message with a simple text body and sends to an activemq queue.
 */

@Startup
@ApplicationScoped
@ContextName("camel-websocket")
public class RouteTimerToQueue extends RouteBuilder {

	Logger logger = Logger.getLogger(RouteTimerToQueue.class.getName());

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Override
	public void configure() throws Exception {
		ActiveMQComponent component = ActiveMQComponent.activeMQComponent();
		component.setConnectionFactory(connectionFactory);

		getContext().addComponent("activemq", component);

		from("timer://myTimer?fixedRate=true&period=5000")
			.routeId("Route1-Timer-Queue")
			.setBody().simple("Sample AMQ Message")
			.log("Created Message: ${body}")
			.to("activemq:queue:Queue1");

	}

}
