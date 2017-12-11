package com.example.hackerdetection.util;

import com.example.hackerdetection.model.LoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * @author Ahmed.Rabie
 * Partitioner class to loadbalance the events based on thier IP,
 * to ensure all events for same IP address forwarded to same Thread handler.
 */
@Slf4j
public class Partitioner extends TailerListenerAdapter {

    private HashMap<Integer, BlockingQueue<String>> queues;
    private int noOfThreads;

    public Partitioner(HashMap<Integer, BlockingQueue<String>> queues, int noOfThreads) {
        this.queues = queues;
        this.noOfThreads = noOfThreads;
    }

    @Override
    public void handle(String line) {
        final int QUEUE_NUMBER = Math.abs(LoginEvent.from(line).getIp().hashCode() % noOfThreads);
        queues.get(QUEUE_NUMBER).add(line);
    }

    @Override
    public void handle(Exception e) {
        log.error("Error detected!", e);
    }
}
