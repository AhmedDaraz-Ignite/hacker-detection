package com.example.hackerdetection.util;

import com.example.hackerdetection.CLArguments;
import org.junit.Test;

import static org.junit.Assert.*;

public class CLArgumentsTest {

    @Test
    public void testValidCLArguments() {
        CLArguments clArguments = new CLArguments(new String[]{"--logfile=C:/temp/in/New Text Document.txt"});
        assertTrue(clArguments.isValid());
    }

    @Test
    public void testInValidCLArguments() {
        CLArguments clArguments = new CLArguments(new String[]{"--path=C:/temp/in/New Text Document.txt"});
        assertFalse(clArguments.isValid());
    }

    @Test
    public void testGetLogFileArgument() {
        CLArguments clArguments = new CLArguments(new String[]{"--logfile=C:/temp/in/New Text Document.txt"});
        assertNotNull(clArguments.getArgument(CLArguments.LOG_FILE));
        assertEquals("C:/temp/in/New Text Document.txt", clArguments.getArgument(CLArguments.LOG_FILE));
    }
}
