package com.kony.adminconsole.keycloakspotlighteventhandler;

import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class KeycloakEventListenerProviderFactory implements EventListenerProviderFactory{

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		
		return new KeycloakEventListenerProvider();
	}

	@Override
	public void init(Scope config) {

		
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
		
	}

	@Override
	public void close() {
		
		
	}

	@Override
	public String getId() {
		
		return "spotlight-keycloak-event-listener";
	}

}
