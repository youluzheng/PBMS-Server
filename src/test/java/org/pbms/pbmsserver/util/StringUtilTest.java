package org.pbms.pbmsserver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class StringUtilTest {
    @ParameterizedTest
    @NullSource
    void testIsValidSizeFormatString_null(String input) {
        assertThrows(NullPointerException.class, () -> StringUtil.isValidSizeFormatString(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "10KB", "20MB", "30GB" })
    void testIsValidSizeFormatString_true(String input) {
        boolean actual = StringUtil.isValidSizeFormatString(input);
        assertEquals(true, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = { "0KB", "10", "1s0KB", "10kb", "20kB", "30Kb", "40mb", "20AB" })
    void testIsValidSizeFormatString_false(String input) {
        boolean actual = StringUtil.isValidSizeFormatString(input);
        assertEquals(false, actual);
    }
}
