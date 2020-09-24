package com.google.calendar.app.domains.events.services;

import com.google.api.services.calendar.model.Event;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
public interface EventService {
    List<Event> findEvents(OAuth2AuthenticationToken authentication) throws IOException, GeneralSecurityException;
}
