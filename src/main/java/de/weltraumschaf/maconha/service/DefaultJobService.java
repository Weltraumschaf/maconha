package de.weltraumschaf.maconha.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.JobInfo;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobExecutor;
import de.weltraumschaf.maconha.job.JobMessage;
import de.weltraumschaf.maconha.job.Jobs;
import de.weltraumschaf.maconha.job.MessageConsumer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Default implementation.
 */
@Service
final class DefaultJobService implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJobService.class);
    private final Receiver messageHandler = new Receiver();
    private final SimpMessagingTemplate webSocket;
    private final JobExecutor executor;
    private final Jobs jobs;

    @Autowired
    public DefaultJobService(final JobExecutor executor, final Jobs jobs, final SimpMessagingTemplate webSocket) {
        super();
        this.executor = Validate.notNull(executor, "executor");
        this.jobs = Validate.notNull(jobs, "jobs");
        this.webSocket = Validate.notNull(webSocket, "webSocket");
    }

    @Override
    public Job<?> create(final String name, final Map<String, Object> config) {
        LOGGER.debug("Create new job for name '{}' with config '{}'.", name, config);
        final Job job = jobs.create(name);
        job.register(messageHandler);
        job.configure(config);
        return job;
    }

    @Override
    public void submit(final Job<?> job) {
        LOGGER.debug("Job submission: {}.", job);
        executor.submit(job);
    }

    @Override
    public void cancel(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(Class<? extends Job> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<JobInfo> list() {
        return executor.list();
    }

    @Override
    public Collection<JobInfo> list(Class<? extends Job> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Async
    @Override
    public void pushChangesToWebSocket() throws InterruptedException {
        LOGGER.debug("Push changes to web socket...");

        while (true) {
            final JobMessage message = messageHandler.take();
            LOGGER.debug("Send message: {}.", message);
            webSocket.convertAndSend("/topic/messages", message);
        }
    }

    private static final class Receiver implements MessageConsumer {

        private final BlockingQueue<JobMessage> messages = new LinkedBlockingQueue<>();

        @Override
        public void receive(final JobMessage message) {
            if (null == message) {
                return;
            }

            try {
                messages.put(message);
            } catch (final InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        public JobMessage take() throws InterruptedException {
            return messages.take();
        }

    }
}
