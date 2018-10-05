package com.spoohapps.farcommon.config;

import com.spoohapps.farcommon.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenLoadingConfigFromMixedArgsTests {

    private int expectedScanTimeoutMs = 1000;
    private int expectedConnectTimeoutMs = 2000;
    private int expectedScanDurationMs = 3000;
    private int expectedControllerPort = 1234;
    private String expectedWhitelistPath = "/path";

    private TestConfig config;

    @BeforeAll
    public void context() {
        String[] args = new String[] {
                "-scanDuration", "" + expectedScanDurationMs,
                "-t", "" + expectedScanTimeoutMs,
                "-connectTimeout", "" + expectedConnectTimeoutMs,
                "-w", expectedWhitelistPath,
                "-controllerPort", "" + expectedControllerPort
        };

        config =
                Config.from(TestConfig.class)
                        .apply(args)
                        .build();
    }

    @Test
    public void ShouldSetScanDuration() {
        assertEquals(expectedScanDurationMs, config.getScanDurationMs());
    }

    @Test
    public void ShouldSetScanTimeout() {
        assertEquals(expectedScanTimeoutMs, config.getScanTimeoutMs());
    }

    @Test
    public void ShouldSetConnectimeout() {
        assertEquals(expectedConnectTimeoutMs, config.getConnectTimeoutMs());
    }

    @Test
    public void ShouldSetControllerPort() {
        assertEquals(expectedControllerPort, config.getControllerPort());
    }

    @Test
    public void ShouldSetWhitelistPath() {
        assertEquals(expectedWhitelistPath, config.getWhitelistPath());
    }

    private interface TestConfig {
        @ConfigFlags({"d", "scanDuration"})
        int getScanDurationMs();

        @ConfigFlags("t")
        int getScanTimeoutMs();

        @ConfigFlags({"c", "connectTimeout"})
        int getConnectTimeoutMs();

        @ConfigFlags({"p", "controllerPort"})
        int getControllerPort();

        @ConfigFlags("w")
        String getWhitelistPath();
    }

}
