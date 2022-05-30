package io.github.navpil.gupai.util;

public class ConsoleOutput {

    private final boolean showHidden;
    private final boolean silent;

    public ConsoleOutput(boolean showHidden, boolean silent) {
        this.showHidden = showHidden;
        this.silent = silent;
    }

    public void sayHidden(String message) {
        if(showHidden && !silent) {
            System.out.println(message);
        }
    }

    public void say(String message) {
        if (!silent) {
            System.out.println(message);
        }
    }

    public void sayAlways(String message) {
        System.out.println(message);
    }


}
