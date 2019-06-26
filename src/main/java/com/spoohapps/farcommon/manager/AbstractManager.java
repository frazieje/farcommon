package com.spoohapps.farcommon.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractManager<T> implements Manager<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractManager.class);

    private final ScheduledExecutorService executorService;

    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    private final long timeout;

    private final TimeUnit timeoutTimeUnit;

    private final long startDelay;

    private final TimeUnit startDelayTimeUnit;

    public AbstractManager(ScheduledExecutorService executorService, ManagerSettings managerSettings) {
        this.executorService = executorService;
        this.timeout = managerSettings.timeout();
        this.timeoutTimeUnit = managerSettings.timeoutTimeUnit();
        this.startDelay = managerSettings.startDelay();
        this.startDelayTimeUnit = managerSettings.startDelayTimeUnit();
    }

    @Override
    public void start() {
        if (doStart()) {
            scheduleTask(startDelay, startDelayTimeUnit);
        } else {
            stop();
        }
    }

    @Override
    public void stop() {
        isStopped.set(true);
        doStop();
    }

    private void process() {
        if (!isStopped.get()) {
            try {
                doProcess();
            } catch (Exception e) {
                logger.error("error processing", e);
            }
            if (timeout > 0) {
                scheduleTask(timeout, timeoutTimeUnit);
            }
        }
    }

    @Override
    public T getResource() {
        if (isStopped.get()) {
            return null;
        } else {
            return doGetResource();
        }
    }

    protected boolean doStart() {
        return true;
    }

    protected abstract void doProcess();

    protected abstract T doGetResource();

    protected void doStop() { }

    private void scheduleTask(long delay, TimeUnit timeUnit) {
        if (!isStopped.get()) {
            executorService.schedule(this::process, delay, timeUnit);
        } else {
            logger.debug(" scheduleTask called while manager stopped, do nothing");
        }
    }


}
