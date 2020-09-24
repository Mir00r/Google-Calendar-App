package com.google.calendar.app.utils;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
public class Constants {

    public static final String APPLICATION_NAME = "GoCalApp";

    public static final String MESSAGE = "message";
    public static final String EVENTS = "events";

    // Exception Message
    public static final String UN_AUTHORIZED_EXCEPTION_MESSAGE = "You're unauthorized to access calendar events.";

    // Routing URL
    public static final String UN_AUTHORIZED_URL = "error/unauthorized";
    public static final String AUTH_LOGIN_PAGE_URL = "auth/login";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String EVENT_URL = "/events";
    public static final String HOME_URL = "/index";
    public static final String EVENT_ALL_URL = "events/all";
    public static final String AUTHORIZATION_REQUEST_BASE_URL = "oauth2/authorization";
}
