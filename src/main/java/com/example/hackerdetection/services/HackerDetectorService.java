package com.example.hackerdetection.services;

import com.example.hackerdetection.model.LoginEvent;
import com.example.hackerdetection.repository.LoginEventRepository;

import java.util.Deque;

public class HackerDetectorService implements HackerDetector {

    private LoginEventRepository repository = new LoginEventRepository();
    private static final int MAX_ATTEMPTS = 5;
    private static final String EMPTY_STRING = "";

    public String parseLogLine(String line) {

        LoginEvent event = LoginEvent.from(line);

        if(LoginEvent.LoginAction.FAILURE.equals(event.getAction())) {
            repository.saveLoginEvent(event);
            Deque<LoginEvent> loginAttempts = repository.findLoginEventsByIP(event.getIp());
            if(loginAttempts.size() >= MAX_ATTEMPTS) {
                if((loginAttempts.getLast().getEventTime() - loginAttempts.getFirst().getEventTime()) % 5000 <= 0) {
                    loginAttempts.removeLast();
                    loginAttempts.add(event);
                    return event.getIp();
                } else {
                    loginAttempts.removeLast();
                    loginAttempts.add(event);
                }
            }
        }
        return EMPTY_STRING;
    }
}
