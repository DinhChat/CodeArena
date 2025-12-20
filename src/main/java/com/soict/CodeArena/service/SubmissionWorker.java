package com.soict.CodeArena.service;

import com.soict.CodeArena.component.JudgeExecutor;
import com.soict.CodeArena.component.SubmissionQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SubmissionWorker {

    private final SubmissionQueue queue;
    private final JudgeExecutor judgeExecutor;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    public SubmissionWorker(SubmissionQueue queue, JudgeExecutor judgeExecutor) {
        this.queue = queue;
        this.judgeExecutor = judgeExecutor;
        startWorker(); // chạy ngay khi app boot
    }

    private void startWorker() {
        executor.submit(() -> {
            while (true) {
                try {
                    Long submissionId = queue.takeSubmission(); // blocking
                    judgeExecutor.runSubmission(submissionId);
                } catch (Exception ignored) {}
            }
        });
    }
}

