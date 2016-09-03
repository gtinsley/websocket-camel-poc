package com.websocket.poc;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "Queue1"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
@ResourceAdapter(value = "activemq-ra.rar")
public class MDBService implements MessageListener {

	Logger logger = Logger.getLogger(MDBService.class.getName());

	@Inject
	@ContextName("camel-websocket")
	CamelContext context;
	ProducerTemplate template;

	public void onMessage(Message message) {
		try {
			String textMessage = ((TextMessage) message).getText();
			logger.info("Message: " + textMessage);

			template = context.createProducerTemplate();
			template.sendBody("direct:consumer", textMessage);

		} catch (JMSException e) {
			throw new EJBException("Error in MDBService", e);
		}
	}

}
