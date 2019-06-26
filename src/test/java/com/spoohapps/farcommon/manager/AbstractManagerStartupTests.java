package com.spoohapps.farcommon.manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractManagerStartupTests {

    private NonStartingTestManager managerUnderTest;

    @BeforeAll
    public void setup() {

        ScheduledExecutorService mockedExecutor = mock(ScheduledExecutorService.class);
        ManagerSettings mockedSettings = mock(ManagerSettings.class);

        managerUnderTest = new NonStartingTestManager(mockedExecutor, mockedSettings);
    }

    @Test
    public void shouldNotBeStarted() {
        assertFalse(managerUnderTest.didGetResource.get());
    }

    @Test
    public void shouldNotRunProcess() {
        assertFalse(managerUnderTest.didRunProcess.get());
    }

    @AfterAll
    public void cleanup() {
        managerUnderTest.stop();
    }

    public class NonStartingTestManager extends AbstractManager<Void> {

        AtomicBoolean didRunProcess = new AtomicBoolean(false);
        AtomicBoolean didGetResource = new AtomicBoolean(false);

        public NonStartingTestManager(ScheduledExecutorService executorService, ManagerSettings managerSettings) {
            super(executorService, managerSettings);
        }

        @Override
        protected boolean doStart() {
            return false;
        }

        @Override
        protected void doProcess() {
            didRunProcess.set(true);
        }

        @Override
        protected Void doGetResource() {
            didGetResource.set(true);
            return null;
        }

    }

}
