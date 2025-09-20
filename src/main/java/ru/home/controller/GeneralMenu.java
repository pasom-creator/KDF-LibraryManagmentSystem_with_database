package ru.home.controller;

import ru.home.util.ConsoleReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class GeneralMenu {
    protected final Map<String, Runnable> GENERAL_MAP;
    protected final String options;

    public GeneralMenu(String options) {
        this.GENERAL_MAP = new HashMap<>();
        this.options = options;
    }

    public void start() {
        while (true) {
            try {
                Runnable runnable = null;
                runnable = GENERAL_MAP.get(ConsoleReader.askQuestion(options));

                if(Objects.nonNull(runnable)) {
                    runnable.run();
                } else {
                    System.out.println("Можнно вводить только цифры. Попробуйте снова");
                }
            } catch (NullPointerException e) {
                System.out.println("Выбрана не верная опция");
            }
        }
    }
}
