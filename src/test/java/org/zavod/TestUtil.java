package org.zavod;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {

    private TestUtil(){}

    public static <T> void assertMatch(T actual, T expected, String... ignoringFields) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoringFields);
    }
}