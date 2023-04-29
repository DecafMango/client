package command;


//import command.ExecuteScript;

import dragon.DragonCreator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public final class CommandManager {

    private static boolean isStarted = false;
    private static boolean isReady = true;
    private static Map<String, Command> commands;
    private static Deque<Request> requestQueue;
    private static boolean isReadyToStop = false;
    private static String login;

    static {
        commands = new HashMap<>();
        requestQueue = new ArrayDeque<>();
    }

    private CommandManager() {
    }

    public static Request validateRequest(String request) {
        if (!requestQueue.isEmpty()) {
            Request nextRequest = requestQueue.pop();
            if (requestQueue.isEmpty())
                isReady = true;
            if (ExecuteScript.getIsStartedByScript() && ExecuteScript.getIsFinishedByScript() && requestQueue.size() == 0)
                isReadyToStop = true;
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
            switch (commandName) {
                case "login":
                    if (requestWords.length != 1) {
                        System.out.println("Команда login не требует наличия дополнительных аргументов");
                        return null;
                    } else {
                        String loginAndPassword = getLoginAndPassword();
                        if (validateLoginAndPassword(loginAndPassword))
                            return new Request(login, commandName, serializeObject(loginAndPassword));
                        else
                            return null;
                    }
                case "register":
                    if (requestWords.length != 1) {
                        System.out.println("Команда register не требует наличия дополнительных аргументов");
                        return null;
                    } else {
                        String loginAndPassword = getLoginAndPassword();
                        if (validateLoginAndPassword(loginAndPassword))
                            return new Request(login, commandName, serializeObject(loginAndPassword));
                        else
                            return null;
                    }
                default:
                    return null;
            }
        } else {
            if (commandName.equals("logout")) {
                System.out.println("Выход из текущей записи");
                isStarted = false;
                login = null;
                return null;
            }
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
                    return new Request(login, commandName, serializeObject(argument));
                else {
                    try {
                        Integer.parseInt(argument);
                    } catch (NumberFormatException e) {
                        System.out.println("Команда " + commandName + " требует наличие целочисленного аргумента.");
                        return null;
                    }
                    return new Request(login, commandName, serializeObject(Integer.parseInt(argument)));
                }

            } else {
                if (requestWords.length != 1) {
                    System.out.println("Команда " + commandName + " записывается без дополнительных аргументов.");
                    return null;
                }
                if (command instanceof CommandWithCreation) {
                    return new Request(login, commandName, serializeObject(DragonCreator.createNewDragon()));
                } else
                    return new Request(login, commandName, null);
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
        commands.put("execute_script", new ExecuteScript());
        isStarted = true;
        System.out.println("Команды обновлены");
    }

    private static String getLoginAndPassword() {
        Scanner scanner = new Scanner(System.in);
        String login = "";
        String password = "";
        StringBuilder sb = new StringBuilder();

        try {
            System.out.print("Введите логин: ");
            login = scanner.nextLine().trim();
            System.out.print("Введите пароль: ");
            password = scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("Принудительное завершение работы программы");
            System.exit(0);
        }
        sb.append(login);
        sb.append(" ");
        sb.append(password);

        CommandManager.login = login;

        return sb.toString();
    }

    private static boolean validateLoginAndPassword(String loginAndPassword) {
        String[] splitLoginAndPassword = loginAndPassword.split(" ");
        if (splitLoginAndPassword.length != 2) {
            System.out.println("Логин и пароль не могут быть пустыми");
            return false;
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

    public static void setIsStarted(boolean isStarted) {
        CommandManager.isStarted = isStarted;
    }

    public static boolean getIsStarted() {
        return isStarted;
    }

    public static boolean getIsReady() {
        return isReady;
    }

    public static boolean getIsReadyToStop() {
        return isReadyToStop;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        CommandManager.login = login;
    }
}
