package com.event.virtugather.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class IOUtilsTest {
    // Tests for the toByteArray method of the IOUtils class.

    @Test
    public void testToByteArray_emptyStream() throws IOException {
        // Create an input stream with no data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Call the method we're testing
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        // Assert that the output is an empty array
        assertArrayEquals(new byte[0], actualOutput);
    }

    @Test
    public void testToByteArray_withData() throws IOException {
        // Create an input stream with some data
        byte[] inputData = new byte[] {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);

        // Call the method we're testing
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        // Assert that the output matches the data we put into the input stream
        assertArrayEquals(inputData, actualOutput);
    }
}