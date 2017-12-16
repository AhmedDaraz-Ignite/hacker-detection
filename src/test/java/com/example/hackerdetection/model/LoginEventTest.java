package com.example.hackerdetection.model;

import com.example.hackerdetection.model.LoginEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginEventTest {

    @Test
    public void testGetLoginEventFromLogLine() {
        LoginEvent event = LoginEvent.from("1507365137,187.218.83.136,John.Smith,FAILURE");
        assertEquals("187.218.83.136", event.getIp());
        assertEquals(1507365137, event.getEventTime());
        assertEquals("FAILURE", event.getAction().name());
    }
}
