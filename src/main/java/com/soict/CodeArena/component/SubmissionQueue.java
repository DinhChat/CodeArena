package com.soict.CodeArena.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SubmissionQueue {

    private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    public void addSubmission(Long submissionId) {
        queue.add(submissionId);
    }

    public Long takeSubmission() throws InterruptedException {
        return queue.take();
    }
}
