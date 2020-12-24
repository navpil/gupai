package io.navpil.github.zenzen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

public class ConsoleInput {

    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public int readInt() {
        final String s;
        try {
            s = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("Cannot read from console: " + e);
            return -1;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Illegal input: " + s);
            return -1;
        }
    }

    public int readInt(Predicate<Integer> successTest, String prompt, String errorMessage) {
        while (true) {
            System.out.println(prompt);
            int i = readInt();
            if (successTest.test(i)) {
                return i;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

}
