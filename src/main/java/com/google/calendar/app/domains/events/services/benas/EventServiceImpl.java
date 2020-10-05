package com.google.calendar.app.domains.events.services.benas;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.calendar.app.AppApplication;
import com.google.calendar.app.domains.events.services.EventService;
import com.google.calendar.app.utils.Constants;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
@Service
public class EventServiceImpl implements EventService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CALENDAR_ID = "primary";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    private final OAuth2AuthorizedClientService authorizedClientService;

    public EventServiceImpl(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public List<Event> findEvents(OAuth2AuthenticationToken authentication) throws IOException, GeneralSecurityException {
        Calendar calendarService = this.prepareCalendarService(authentication);

        // Use this to build event list from calendar api, https://developers.google.com/calendar/quickstart/java
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = calendarService.events().list(CALENDAR_ID)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
        return items;
    }

    /**
     * Needed help to build credential object, as calendar api doc has their client jetty implementation
     * https://github.com/a2cart/google-calendar-api/blob/master/src/main/java/com/api/controllers/GoogleCalController.java
     *
     * @param authentication
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private Calendar prepareCalendarService(OAuth2AuthenticationToken authentication) throws IOException, GeneralSecurityException {
        // Get authorized client through Oauth2 client service
        OAuth2AuthorizedClient client = this.authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, this.mapToClientSecrets(client), SCOPES)
                .build();

        // Build calendar and set application name, token, refresh token response
        Credential credential = flow.createAndStoreCredential(this.createTokenResponse(client), authentication.getName());

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();
    }

    /**
     * Mapping/Load google calendar application client secret information
     *
     * @param client
     * @return
     */
    private GoogleClientSecrets mapToClientSecrets(OAuth2AuthorizedClient client) {
        GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
        web.setClientId(client.getClientRegistration().getClientId());
        web.setClientSecret(client.getClientRegistration().getClientSecret());
        web.setAuthUri(client.getClientRegistration().getProviderDetails().getAuthorizationUri());
        web.setTokenUri(client.getClientRegistration().getProviderDetails().getTokenUri());
        return new GoogleClientSecrets().setWeb(web);
    }

    /**
     * Create token response through oauth2 client response and mapped token, expired time, refresh token and scopes
     *
     * @param client
     * @return
     */
    private TokenResponse createTokenResponse(OAuth2AuthorizedClient client) {
        TokenResponse response = new TokenResponse();
        response.setAccessToken(client.getAccessToken().getTokenValue());
        if (client.getRefreshToken() != null)
            response.setRefreshToken(client.getRefreshToken().getTokenValue());

        Duration duration = Duration.between(Instant.now(), client.getAccessToken().getExpiresAt());
        response.setExpiresInSeconds(duration.getSeconds());

        response.setTokenType(client.getAccessToken().getTokenType().getValue());
        response.setScope(client.getAccessToken().getScopes().toString());
        return response;
    }

}
