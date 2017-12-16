package com.example.hackerdetection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginEvent {

    public enum LoginAction  {SUCCESS, FAILURE};

    private long eventTime;
    private String ip;
    private LoginAction action;

    public static LoginEvent from(String rawEvent) {
        final String[] tokens = rawEvent.split(",");
        LoginEvent event = new LoginEvent(Long.parseLong(tokens[0]), tokens[1], LoginAction.valueOf(tokens[3]));
        return event;
    }
}
