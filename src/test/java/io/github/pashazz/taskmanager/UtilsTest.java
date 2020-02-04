package io.github.pashazz.taskmanager;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    public static Scanner getScannerForString(String text) {
        return new Scanner(new ByteArrayInputStream(text.getBytes()));
    }

    @Test
    void getWordOrQuotedText () {
        String input = "unquoted \"quoted text\"";
        var scanner = getScannerForString(input);
        String next = Utils.getWordOrQuotedText(scanner);
        assertEquals("unquoted", next);
        next = Utils.getWordOrQuotedText(scanner);
        assertEquals("quoted text", next);
        next = Utils.getWordOrQuotedText(scanner);
        assertNull(next);
    }
}