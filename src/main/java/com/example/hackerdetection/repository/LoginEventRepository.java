package com.example.hackerdetection.repository;

import com.example.hackerdetection.model.LoginEvent;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Ahmed.Rabie
 * data access repository. currently it is backed by Hashmap, but in reality it will be backed by NoSql storage.
 */
public class LoginEventRepository {

    private Map<String, Deque<LoginEvent>> attempts = new HashMap<>();

    public void saveLoginEvent(LoginEvent event) {
        if(attempts.get(event.getIp()) == null) {
            Deque<LoginEvent> deque = new LinkedList<>();
            deque.add(event);
            attempts.put(event.getIp(), deque);
        } else {
            attempts.get(event.getIp()).add(event);
        }
    }

    public Deque<LoginEvent> findLoginEventsByIP(String ip) {
        return attempts.get(ip);
    }
}
