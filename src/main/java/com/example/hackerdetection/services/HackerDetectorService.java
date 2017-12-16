package com.example.hackerdetection.services;

import com.example.hackerdetection.model.LoginEvent;
import com.example.hackerdetection.repository.LoginEventRepository;

import java.util.Deque;

/**
 * @author Ahmed.Rabie
 * Hacker detection service, it has access to a nosql data storage with the IP, DEQUEUE of max of 5 items length.
 * It get th time difference to calculate the time window and decide if the IP is a potential threat or not.
 */
public class HackerDetectorService implements HackerDetector {

    private LoginEventRepository repository = new LoginEventRepository();
    private static final String EMPTY_STRING = "";
    private long timeWindow;
    private int maxAttempts;

    public HackerDetectorService(long timeWindow, int maxAttempts) {
        this.timeWindow = timeWindow;
        this.maxAttempts = maxAttempts;
    }

    public String parseLogLine(String line) {

        LoginEvent event = LoginEvent.from(line);

        if(LoginEvent.LoginAction.FAILURE.equals(event.getAction())) {
            repository.saveLoginEvent(event);
        }

        Deque<LoginEvent> loginAttempts = repository.findLoginEventsByIP(event.getIp());
        if(loginAttempts.size() >= maxAttempts) {
            if(getTimeDifference(loginAttempts) / timeWindow == 0) {
                loginAttempts.removeLast();
                loginAttempts.add(event);
                return event.getIp();
            } else {
                loginAttempts.removeLast();
                loginAttempts.add(event);
            }
        }

        return EMPTY_STRING;
    }

    private long getTimeDifference(Deque<LoginEvent> loginAttempts) {
        return loginAttempts.getLast().getEventTime() - loginAttempts.getFirst().getEventTime();
    }
}
