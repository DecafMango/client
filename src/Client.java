import command.CommandManager;
import command.CommandResult;
import command.Request;
import command.client_command.ObjectSerializer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private static Scanner scanner = new Scanner(System.in);
    private final static String CLIENT_HOSTNAME = "localhost";
    private final static int CLIENT_PORT = 1234;
    private final static String SERVER_HOSTNAME = "localhost";
    private final static int SERVER_PORT = 10000;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        DatagramChannel client = startCLient();
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
            CommandResult serverAnswer = (CommandResult) ObjectSerializer.deserializeObject(inputPacket.getData());

            if (clientRequest.getCommandName().equals("start")) {
                CommandManager.initCommands(serverAnswer.getDefinition());
                return;
            }
            else {
                System.out.println(serverAnswer.getDefinition());
                return;
            }
        }
    }

    private static Request getRequest() {

        if (!CommandManager.getIsReady())
            return CommandManager.validateRequest(null);

        String request = null;
        while (true) {
            if (!CommandManager.getIsStarted())
                System.out.println("Чтобы продолжить дальнейшую работу на сервере - введите команду start.");
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