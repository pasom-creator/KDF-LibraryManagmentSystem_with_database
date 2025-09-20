package ru.home.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ConsoleReader {
    private final static BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    private ConsoleReader() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    public static String askQuestion(String question) {
        System.out.println(question);
        try {
            return READER.readLine().strip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
