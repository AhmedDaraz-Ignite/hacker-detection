package com.example.hackerdetection.repository;

import com.example.hackerdetection.model.LoginEvent;
import com.example.hackerdetection.repository.LoginEventRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Deque;

import static org.junit.Assert.assertEquals;

public class LoginEventRepositoryTest {

    private LoginEventRepository repository;

    @Before
    public void setup() {
        this.repository = new LoginEventRepository();
    }

    @Test
    public void testSaveLoginEvent() {
        LoginEvent event = LoginEvent.from("1507365137,187.218.83.136,John.Smith,FAILURE");
        repository.saveLoginEvent(event);

        Deque<LoginEvent> savedEvent = repository.findLoginEventsByIP(event.getIp());
        assertEquals(1, savedEvent.size());

        LoginEvent eventElement = savedEvent.element();
        assertEquals(event.getIp(), eventElement.getIp());
        assertEquals(event.getEventTime(), eventElement.getEventTime());
        assertEquals(event.getAction(), eventElement.getAction());
    }
}
