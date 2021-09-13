package org.pbms.pbmsserver.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {
    @ParameterizedTest
    @NullSource
    void testIsValidSizeFormatString_null(String input) {
        assertThrows(NullPointerException.class, () -> StringUtil.isValidSizeFormatString(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"10KB", "20MB", "30GB"})
    void testIsValidSizeFormatString_true(String input) {
        boolean actual = StringUtil.isValidSizeFormatString(input);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0KB", "10", "1s0KB", "10kb", "20kB", "30Kb", "40mb", "20AB"})
    void testIsValidSizeFormatString_false(String input) {
        boolean actual = StringUtil.isValidSizeFormatString(input);
        assertFalse(actual);
    }
}
