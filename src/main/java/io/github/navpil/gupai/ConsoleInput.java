package io.github.navpil.gupai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public <T> T choice(List<T> choices, boolean zeroAllowed, String question) {
        final StringBuilder sb = new StringBuilder(question).append("\n");
        if (zeroAllowed) {
            sb.append("0) None\n");
        }
        for (int i = 0; i < choices.size(); i++) {
            sb.append(i + 1).append(") ").append(choices.get(i)).append("\n");
        }
        final int choice = readInt(
                i -> i >= (zeroAllowed ? 0 : 1) && i <= choices.size(),
                sb.toString(),
                "Invalid input"
        );
        if (choice == 0) {
            return null;
        }
        return choices.get(choice - 1);
    }

    public <T> List<T> multiChoice(List<T> choices, boolean zeroAllowed, String question) {
        return multiChoice(choices, zeroAllowed, question, (integer -> true));
    }

    public <T> List<T> multiChoice(List<T> choices, boolean zeroAllowed, String question, int sizeRestriction) {
        return multiChoice(choices, zeroAllowed, question, (i) -> i == sizeRestriction);
    }

    private <T> List<T> multiChoice(List<T> choices, boolean zeroAllowed, String question, Predicate<Integer> sizeRestriction) {
        final StringBuilder sb = new StringBuilder(question).append("\n");
        sb.append("Choose several separated by comma").append("\n");
        if (zeroAllowed) {
            sb.append("0) None\n");
        }
        for (int i = 0; i < choices.size(); i++) {
            sb.append(i + 1).append(") ").append(choices.get(i)).append("\n");
        }

        Predicate<String> validate = (s) -> {
            if (zeroAllowed && "0".equals(s)) {
                return true;
            }
            final HashSet<String> validValues = new HashSet<>();
            for (int i = 1; i <= choices.size(); i++) {
                validValues.add("" + i);
            }
            final List<String> values = Arrays.asList(s.split(","));
            //Do not allow duplicates, do not allow empty values, has to contains strictly integers separated by commas
            return new HashSet<>(values).size() == values.size() && !values.isEmpty() && validValues.containsAll(values) && sizeRestriction.test(values.size());
        };

        final String values = readString(
                validate,
                sb.toString(),
                "Invalid input"
        );
        if ("0".equals(values)) {
            return Collections.emptyList();
        }
        return Arrays.stream(values.split(",")).map(Integer::parseInt).map(i -> i-1).map(choices::get).collect(Collectors.toList());
    }



    public String readString(Predicate<String> successTest, String prompt, String errorMessage) {
        while (true) {
            System.out.println(prompt);
            String s = "";
            try {
                s = bufferedReader.readLine();
            } catch (IOException ignore) {
            }
            if (successTest.test(s)) {
                return s;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

}
