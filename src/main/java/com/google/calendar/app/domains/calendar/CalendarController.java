package com.google.calendar.app.domains.calendar;

import com.google.calendar.app.domains.events.services.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author mir00r on 5/10/20
 * @project IntelliJ IDEA
 */
@RestController
@RequestMapping("/cal")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public void startApp() {
        try {
            calendarService.start();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
