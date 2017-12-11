package com.example.hackerdetection.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginEvent {

    public enum LoginAction  {SUCCESS, FAILURE};

    private String ip;
    private long eventTime;
    private LoginAction action;
    private String userName;

    public static LoginEvent from(String rawEvent) {
        final String[] tokens = rawEvent.split(",");
        LoginEvent event = new LoginEvent(tokens[1], Long.parseLong(tokens[0]), LoginAction.valueOf(tokens[3]), tokens[2]);
        return event;
    }
}
