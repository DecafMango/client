import command.CommandManager;
import command.Response;
import command.Request;
import command.ExecuteScript;
import command.ObjectSerializer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private static Scanner scanner = new Scanner(System.in);
    private final static String CLIENT_HOSTNAME = "localhost";
    private final static int CLIENT_PORT = 0;
    private final static String SERVER_HOSTNAME = "localhost";
    private final static int SERVER_PORT = 10000;

    private static boolean isStartedByScript = false;
    private static boolean isAlreadyStartedByScript = false;
    private static boolean isFinished = false;
    private static String scriptPath = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramChannel client = startCLient();

        if (args.length != 0) {
            if (args.length == 1) {
                isStartedByScript = true;
                scriptPath = args[0];
                ExecuteScript.setIsStartedByScript(true);
                ExecuteScript.setStartedFilePath(scriptPath);
            } else {
                System.out.println("Для запуска программы либо отсутствует аргумент или " +
                        "прописывается путь до файла с алгоритмом.");
                System.exit(0);
            }
        } else
            isStartedByScript = false;

        while (true) {
            workWithServer(client);
        }
    }

    private static DatagramChannel startCLient() throws IOException {
        DatagramChannel client = DatagramChannel.open();
        SocketAddress clientAddress = new InetSocketAddress(CLIENT_HOSTNAME, CLIENT_PORT);
        try {
            client.bind(clientAddress);
        } catch (BindException e) {
            System.out.println("Клиент с таким портом на данном устройстве уже существует.\n" +
                    "Поменяйте порт клиента.");
            System.exit(0);
        }
        SocketAddress serverAddress = new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT);
        client.connect(serverAddress);
        client.socket().setSoTimeout(3000);
        System.out.println("Клиент запущен:\nАдрес клиента - " + clientAddress + "\nАдрес сервера: " + serverAddress);
        return client;
    }

    private static void workWithServer(DatagramChannel client) throws IOException, ClassNotFoundException,
            PortUnreachableException, SocketTimeoutException {
        Request clientRequest = getRequest();

        client.configureBlocking(false);
        byte[] serializedRequest = ObjectSerializer.serializeObject(clientRequest);
        ByteBuffer buffer = ByteBuffer.wrap(serializedRequest);
        int attemptsCount = 0;

        nextAttempt:
        while (attemptsCount < 3) {

            client.send(buffer, client.getRemoteAddress());
            client.configureBlocking(true);
            DatagramPacket inputPacket = new DatagramPacket(new byte[1024 * 1024], 1024 * 1024);
            try {
                client.socket().receive(inputPacket);
            } catch (PortUnreachableException e) {
                System.out.println("Сервер на данный момент отключен - попробуйте подключиться позже.");
                if (isStartedByScript) {
                    System.out.println("Завершение работы программы.");
                    System.exit(0);
                }
                CommandManager.setIsStarted(false);
                return;
            } catch (SocketTimeoutException e) {
                if (attemptsCount == 2) {
                    System.out.println("Сервер определенно не отвечает - повторите попытку позже.");
                    CommandManager.setIsStarted(false);
                    return;
                }
                System.out.println("Сервер не отвечает - повторное отправление запроса.");
                attemptsCount++;
                continue nextAttempt;
            }
            Response serverResponse = (Response) ObjectSerializer.deserializeObject(inputPacket.getData());
            String responseDefinition = serverResponse.getDefinition();

            switch (clientRequest.getCommandName()) {
                case "login":
                    if (responseDefinition.equals("Неверный логин или пароль"))
                        System.out.println(responseDefinition);
                    else {
                        System.out.println("Соединение с сервером установлено");
                        CommandManager.setLogin(clientRequest.getLogin());
                        CommandManager.setIsStarted(true);
                        CommandManager.initCommands(responseDefinition);
                    }
                    break nextAttempt;
                case "update":
                    CommandManager.initCommands(responseDefinition);
                default:
                    System.out.println(responseDefinition);
                    if (CommandManager.getIsReadyToStop()) {
                        System.out.println("Завершение работы программы");
                        System.exit(0);
                    }
                    break nextAttempt;
            }
        }
    }

    private static Request getRequest() throws IOException {

        if (isStartedByScript && !CommandManager.getIsStarted())
            return CommandManager.validateRequest("start");
        else if (isStartedByScript && !isAlreadyStartedByScript) {
            isAlreadyStartedByScript = true;
            return CommandManager.validateRequest("execute_script " + scriptPath);
        }

        if (!CommandManager.getIsReady())
            return CommandManager.validateRequest(null);

        String request = null;
        while (true) {
            if (!CommandManager.getIsStarted())
                System.out.println("Введите одну из команд:\n\t--> login (авторизация)\n\t--> register (регистрация)");
            System.out.print("--> ");
            try {
                request = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Принудительное завершение работы программы.");
                System.exit(0);
            }
            if (request.isBlank())
                continue;

            Request requestObject = CommandManager.validateRequest(request);

            if (requestObject != null) {
                return requestObject;
            }
        }
    }
}