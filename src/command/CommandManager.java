package command;


import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public final class CommandManager {
    private static volatile Map<String, Command> commands;
    private static String login;

    static {
        commands = new HashMap<>();
    }

    private CommandManager() {
    }


    public static void initCommands(String meta) {
        String[] allCommands = meta.split(";");

        for (String command : allCommands) {
            String[] commandOptions = command.split("\\.");
            String commandName = commandOptions[0];
            String commandClass = commandOptions[1];

            switch (commandClass) {
                case "1":
                    commands.put(commandName, new Command());
                    break;
                case "2":
                    String argumentType = commandOptions[2];
                    commands.put(commandName, new CommandWithArgument(Integer.parseInt(argumentType)));
                    break;
                case "3":
                    commands.put(commandName, new CommandWithCreation());
            }
        }
        System.out.println("Команды обновлены");
    }

    public static boolean validateLoginAndPassword(String login, String password) {
        if (login.isBlank())
            return false;
        if (password.isBlank())
            return false;
        return true;
    }

    public static boolean validateArgument(String argument, boolean checkInt) {
        if (argument.isBlank())
            return false;

        if (checkInt) {
            try {
                Integer.parseInt(argument);
            } catch (InputMismatchException e) {
                return false;
            }
        }

        return true;
    }

    private static byte[] serializeObject(Object o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(o);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Command> getCommands() {
        return commands;
    }
    public static String getLogin() {
        return login;
    }
    public static void setLogin(String login) {
        CommandManager.login = login;
    }
}
