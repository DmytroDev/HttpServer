package com.infopulse.service;

public class CalculateCommand {
    private String command;
    private int firstParameter;
    private int secondParameter;
    private boolean isValid;

    public CalculateCommand(){
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(int firstParameter) {
        this.firstParameter = firstParameter;
    }

    public int getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(int secondParameter) {
        this.secondParameter = secondParameter;
    }

    @Override
    public String toString() {
        return "CalculateCommand{" +
                "command='" + command + '\'' +
                ", firstParameter=" + firstParameter +
                ", secondParameter=" + secondParameter +
                ", isValid=" + isValid +
                '}';
    }
}
