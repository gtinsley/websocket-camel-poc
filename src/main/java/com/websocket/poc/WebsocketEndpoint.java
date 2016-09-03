package com.websocket.poc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

@ApplicationScoped
@ServerEndpoint("/websocket")
public class WebsocketEndpoint {
	//experiment with this using the ahc endpoint

	Logger logger = Logger.getLogger(WebsocketEndpoint.class.getName());
	
	private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(byte[] message, Session session) {
		logger.info("Session ID: " + session.getId() + " Message: " + message);

		for (Session peer : peers) {
			if (!peer.equals(session)) {
				try {
					peer.getBasicRemote().sendObject(message);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (EncodeException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		peers.add(session);
		logger.info("Websocket opened: " + session.getId());
	}

	@OnClose
	public void onclose(Session session, CloseReason reason) {
		peers.remove(session);
		logger.info("Closing Websocket reason: " + reason.getReasonPhrase());
	}

	@OnError
	public void onError(Throwable error) {

	}

}
