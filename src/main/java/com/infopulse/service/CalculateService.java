package com.infopulse.service;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculateService {
    private String urlPattern;
    private boolean isValidCommand;

    public static Logger LOG = Logger.getLogger(CalculateService.class.getName());

    public CalculateService(String urlPattern) {
        this.urlPattern = urlPattern;
        this.isValidCommand = true;
    }

     /*
     parse urlPattern and define command, first parameter, second parameter
     http://127.0.0.1:3000/calculate?operation=add&operand1=5&operand2=10
     */
    public CalculateCommand parseUrlPattern(){
        CalculateCommand command = new CalculateCommand();

        if (urlPattern.contains("operation=") && urlPattern.contains("operand1=") && (urlPattern.contains("operand2="))){
            command.setValid(true);
        } else {
            command.setValid(false);
            LOG.warning("URL pattern is invalid. Pattern: " + urlPattern);
        }

        String [] tokens = urlPattern.split("[?&]");
        command.setCommand(tokens[1].split("=")[1]);
        command.setFirstParameter(Integer.parseInt(tokens[2].split("=")[1]));
        command.setSecondParameter(Integer.parseInt(tokens[3].split("=")[1]));
        LOG.info(command.toString());

        return command;
    }

    public int calculate(CalculateCommand command) {
        switch (command.getCommand()){
            case Operations.ADDITION: {
                return command.getFirstParameter() + command.getSecondParameter();
            }
            case Operations.SUBSTRACTION: {
                return command.getFirstParameter() - command.getSecondParameter();
            }
            case Operations.MULTIPLICATION: {
                return command.getFirstParameter() * command.getSecondParameter();
            }
            case Operations.DIVISION: {
                return command.getFirstParameter() / command.getSecondParameter();
            }
            default: {
                LOG.log(Level.WARNING, "Incorrect url command or parameters:\n" +
                        "command= " + command.getCommand() +
                        "firstParameter= " + command.getFirstParameter() +
                        "secondParameter= " + command.getSecondParameter());
                isValidCommand = false;
                break;
            }
        }
        return 0;
    }

    private static final class Operations{

        public final static String ADDITION = "add";
        public final static String SUBSTRACTION = "sub";
        public final static String MULTIPLICATION = "mul";
        public final static String DIVISION = "div";
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public boolean isValidCommand() {
        return isValidCommand;
    }

    public void setValidCommand(boolean validCommand) {
        isValidCommand = validCommand;
    }

}
