package fr.inria.diverse;

import java.io.IOException;

/**
 * Created by aelie on 19/06/14.
 */
public class ConsoleDisplay {
    public static void displayMessageProgress(int current, int max, String message) throws IOException {
        String clearConsole = System.getProperty("os.name").contains("Windows")?"cls":"clear";
        Runtime.getRuntime().exec(clearConsole);
        System.out.println(message + current + "/" + max);
    }

    public static void displayMessagePercent(int current, int max, String message) throws IOException {
        String clearConsole = System.getProperty("os.name").contains("Windows")?"cls":"clear";
        Runtime.getRuntime().exec(clearConsole);
        System.out.println(message + (current / max * 100) + "%");
    }

    public static void displayMessageCurrent(int current, String message) throws IOException {
        String clearConsole = System.getProperty("os.name").contains("Windows")?"cls":"clear";
        Runtime.getRuntime().exec(clearConsole);
        System.out.println(message + current);
    }
}
