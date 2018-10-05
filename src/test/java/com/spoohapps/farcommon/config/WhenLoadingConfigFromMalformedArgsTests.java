package com.spoohapps.farcommon.config;

import com.spoohapps.farcommon.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WhenLoadingConfigFromMalformedArgsTests {

    @Test
    public void ShouldIgnoreUnintelligibleValues() {
        int expectedDuration = 1000;
        String[] args = new String[] {"-lkdsa", "" + expectedDuration};
        TestConfig config =
                Config.from(TestConfig.class)
                        .apply(args)
                        .build();

        assertNull(config.getWhitelistPath());
    }

    @Test
    public void ShouldIgnoreRandomUnintelligibleValues() {
        int expectedDuration = 1000;
        String[] args = new String[] {"lkdsa", "" + expectedDuration};
        TestConfig config =
                Config.from(TestConfig.class)
                        .apply(args)
                        .build();

        assertEquals(0, config.getScanDurationMs());
    }

    @Test
    public void ShouldIgnoreUnintelligibleValuesWhenValidValuesPresent() {
        int expectedDuration = 1000;
        String[] args = new String[] {"lkdsa", "" + 10, "-d", "" + expectedDuration};
        TestConfig config =
                Config.from(TestConfig.class)
                        .apply(args)
                        .build();
        assertEquals(expectedDuration, config.getScanDurationMs());
    }

    private interface TestConfig {
        @ConfigFlags("d")
        int getScanDurationMs();

        @ConfigFlags("w")
        String getWhitelistPath();
    }

}
