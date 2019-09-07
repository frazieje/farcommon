package com.spoohapps.farcommon.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractManager<T> implements Manager<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractManager.class);

    private final ScheduledExecutorService executorService;

    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    private final int[] backoffMultipliers = { 1, 2, 4, 8, 16, 32 };

    private int backoff = 0;

    private float backoffScale;

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
        this.backoffScale = managerSettings.backoffScale();
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
                if (backoff > 0) {
                    backoff = 0;
                }
            } catch (Exception e) {
                logger.error("error processing", e);
                if (backoff != (backoffMultipliers.length - 1)) {
                    backoff++;
                }
            }
            if (timeout > 0) {
                scheduleTask((long) (timeout * (backoffMultipliers[backoff] * (backoff != 0 ? backoffScale : 1))), timeoutTimeUnit);
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
