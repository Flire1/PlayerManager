package com.Flire2;

public class Option {
    private final String label;
    private final Runnable action;

    public Option(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public Runnable getAction() {
        return action;
    }
}
