package com.example.hackerdetection.services;

import com.example.hackerdetection.services.HackerDetector;
import com.example.hackerdetection.services.HackerDetectorService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HackerDetectorServiceTest {

    private static final int TIME_WINDOW = 5000;
    private static final int MAX_ATTEMPTS = 5;

    private HackerDetector detector;

    @Before
    public void setUp() {
        detector = new HackerDetectorService(TIME_WINDOW, MAX_ATTEMPTS);
    }

    @Test
    public void testParseLogLineNoAttackDetected() {
        detector.parseLogLine("1507365137,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507372137,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507378137,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507384137,187.218.83.136,John.Smith,FAILURE");

        String emptyString = detector.parseLogLine("1507396137,187.218.83.136,John.Smith,FAILURE");

        assertTrue(emptyString == "");
    }

    @Test
    public void testParseLogLineAttackDetected() {
        detector.parseLogLine("1507365137,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507365437,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507365537,187.218.83.136,John.Smith,FAILURE");
        detector.parseLogLine("1507365837,187.218.83.136,John.Smith,FAILURE");

        String ip = detector.parseLogLine("1507366037,187.218.83.136,John.Smith,FAILURE");
        assertTrue("187.218.83.136".equals(ip));
    }
}
