package command;


//import command.client_command.ExecuteScript;

import command.client_command.ExecuteScript;
import command.client_command.Exit;
import command.client_command.Start;
import dragon.DragonCreator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager {

    private static boolean isStarted = false;
    private static boolean isReady = true;

    private static Map<String, Command> commands;
    private static Deque<Request> requestQueue;

    static {
        commands = new HashMap<>();
        requestQueue = new ArrayDeque<>();

        commands.put("start", new Start());
        commands.put("exit", new Exit());
        commands.put("execute_script", new ExecuteScript());
    }

    private CommandManager() {}

    public static Request validateRequest(String request) {
        if (!requestQueue.isEmpty()) {
            Request nextRequest = requestQueue.pop();
            if (requestQueue.isEmpty())
                isReady = true;
            return nextRequest;
        }

        request = request.trim();
        while (request.contains("  "))
            request = request.replace("  ", " ");
        String[] requestWords = request.split(" ");
        String commandName = requestWords[0];

        if (commandName.equals("exit")) {
            if (requestWords.length != 1) {
                System.out.println("Команда exit не требует наличия дополнительных аргументов.");
                return null;
            }
            System.out.println("Выход из программы.");
            System.exit(0);
        }

        if (!isStarted) {
           if (!commandName.equals("start")) {
               return null;
           } else {
               if (requestWords.length != 1) {
                   System.out.println("Команда start не требует наличия дополнительных аргументов.");
                   return null;
               } else {
                   System.out.println("Соединение с сервером.");
                   isStarted = true;
                   return new Request(commandName, null);
               }
           }
        } else {
            if (!commands.containsKey(commandName)) {
                System.out.println("Команды " + commandName + " не существует.\n" +
                        "Попробуйте перезапустить приложение при помощи команды start.\n" +
                        "Если предыдущее действие не помогло, то такой команды действительно не существует.\n" +
                        "Для справки воспользуйтесь командой help.");
                return null;
            }
            Command command = commands.get(commandName);
            if (command instanceof CommandWithArgument) {
                if (requestWords.length != 2) {
                    System.out.println("Команда " + commandName + " требует наличия одного аргумента:\n" +
                     commandName + " <аргумент> (стрелки писать не нужно - это для акцентирования внимания.");
                    return null;
                }
                String argument = requestWords[1];
                if (commandName.equals("execute_script")) {
                    requestQueue = ExecuteScript.execute(argument);
                    if (requestQueue.isEmpty())
                        return null;
                    isReady = false;
                    Request request1 = requestQueue.pop();
                    if (requestQueue.isEmpty())
                        isReady = true;
                    return request1;
                }
                if (((CommandWithArgument) command).getArgumentType() == 1)
                    return new Request(commandName, serializeObject(argument));
                else {
                    try {
                        Integer.parseInt(argument);
                    } catch (NumberFormatException e) {
                        System.out.println("Команда " + commandName + " требует наличие целочисленного аргумента.");
                        return null;
                    }
                    return new Request(commandName, serializeObject(Integer.parseInt(argument)));
                }

            } else  {
                if (requestWords.length != 1) {
                    System.out.println("Команда " + commandName + " записывается без дополнительных аргументов.");
                    return null;
                }
                if (command instanceof CommandWithCreation) {
                    return new Request(commandName, serializeObject(DragonCreator.createNewDragon()));
                } else
                    return new Request(commandName, null);
            }
        }
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
        System.out.println("Соединение с сервером установлено. Команды инициализированы.");
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

    public static void setIsStarted(boolean isStarted) {
        CommandManager.isStarted = isStarted;
    }

    public static boolean getIsStarted() {
        return isStarted;
    }

    public static boolean getIsReady() {
        return isReady;
    }
}