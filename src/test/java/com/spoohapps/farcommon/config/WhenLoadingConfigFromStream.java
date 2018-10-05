package com.spoohapps.farcommon.config;

import com.spoohapps.farcommon.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenLoadingConfigFromStream {

    private TestConfig config;

    @BeforeAll
    public void context() {

        List<String> lines = Arrays.asList(
                "scanDuration=1000",
                "connectTimeout=500",
                "t=750",
                "p=1234",
                "whitelistPath=/etc/jble6lowpand"
        );

        String data = String.join(System.lineSeparator(), lines);

        InputStream configStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        config = Config.from(TestConfig.class)
                .apply(configStream)
                .build();
    }

    @Test
    public void ShouldSetScanDuration() {
        assertTrue(config.getScanDurationMs() > 0);
    }

    @Test
    public void ShouldSetScanTimeout() {
        assertTrue(config.getScanTimeoutMs() > 0);
    }

    @Test
    public void ShouldSetConnectTimeout() {
        assertTrue(config.getConnectTimeoutMs() > 0);
    }

    @Test
    public void ShouldSetControllerPort() {
        assertTrue(config.getControllerPort() > 0);
    }

    @Test
    public void ShouldSetWhitelistPath() {
        assertNotNull(config.getWhitelistPath());
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

        @ConfigFlags({"w", "whitelistPath"})
        String getWhitelistPath();
    }

}
