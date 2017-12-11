package com.example.hackerdetection.services;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * @author Ahmed.Rabie
 * The log event processor, parsre the event and check the returned IP if it is a threat or not.
 */
@Slf4j
public class LoginEventProcessor implements Runnable {

    private HackerDetector detector;
    private BlockingQueue<String> queue;

    public LoginEventProcessor(HackerDetector detector, BlockingQueue queue) {
        this.detector = detector;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String loginEventLine;
            while((loginEventLine = queue.take()) != null) {
                String ip = detector.parseLogLine(loginEventLine);
                handleLoginEventResponse(ip);
            }
        } catch (InterruptedException e) {
            log.error("Error detected!", e);
        }
    }

    public void handleLoginEventResponse(String ip) {
        // TODO currently it write to standard output, it should send to file.
        if(ip != null) {
            log.info("{},   {}", Thread.currentThread().getName(), ip);
        }
    }
}
