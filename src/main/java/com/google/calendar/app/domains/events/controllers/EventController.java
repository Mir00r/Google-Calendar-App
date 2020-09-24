package com.google.calendar.app.domains.events.controllers;

import com.google.api.services.calendar.model.Event;
import com.google.calendar.app.domains.events.services.EventService;
import com.google.calendar.app.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
@Controller
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(Constants.EVENT_URL)
    private String events(OAuth2AuthenticationToken authentication, Model model) throws IOException, GeneralSecurityException {

        List<Event> items = this.eventService.findEvents(authentication);

        model.addAttribute(Constants.EVENTS, items);
        return Constants.EVENT_ALL_URL;
    }
}
