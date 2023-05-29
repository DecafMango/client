package client;

import command.CommandManager;
import command.Response;
import command.Request;
import command.ObjectSerializer;
import gui.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Client {

    private final static String CLIENT_HOSTNAME = "localhost";
    private final static int CLIENT_PORT = 0;
    private final static String SERVER_HOSTNAME = "localhost";
    private final static int SERVER_PORT = 10000;
    private static DatagramChannel channel;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        StartWindow.show();
    }

    private static boolean startClient() {
        try {
            DatagramChannel channel = DatagramChannel.open();
            SocketAddress clientAddress = new InetSocketAddress(CLIENT_HOSTNAME, CLIENT_PORT);
            channel.bind(clientAddress);
            SocketAddress serverAddress = new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT);
            channel.connect(serverAddress);
            channel.socket().setSoTimeout(1000);
            Client.channel = channel;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Response establishConnection(String login, String password, boolean isRegistration) {
        if (!startClient())
            return new Response(Language.getBundle().getString("applicationError"), 1);
        if (!CommandManager.validateLoginAndPassword(login, password))
            return new Response(Language.getBundle().getString("invalid"), 1);

        String loginAndPassword = login + " " + password;
        Request request;
        try {
            if (!isRegistration)
                request = new Request(login, "login", ObjectSerializer.serializeObject(loginAndPassword), Language.getLanguage());
            else
                request = new Request(login, "register", ObjectSerializer.serializeObject(loginAndPassword), Language.getLanguage());
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(Language.getBundle().getString("applicationError"), 1);
        }

        return workWithServer(request);
    }

    public static Response workWithServer(Request request) {
        try {
            channel.configureBlocking(false);
            byte[] serializedRequest = ObjectSerializer.serializeObject(request);
            ByteBuffer buffer = ByteBuffer.wrap(serializedRequest);
            int attemptsCount = 0;

            while (attemptsCount < 3) {
                channel.send(buffer, channel.getRemoteAddress());
                channel.configureBlocking(true);
                DatagramPacket inputPacket = new DatagramPacket(new byte[1024 * 1024], 1024 * 1024);
                try {
                    channel.socket().receive(inputPacket);
                } catch (PortUnreachableException e) {
                    return new Response(Language.getBundle().getString("serverSleep"), 1);
                } catch (SocketTimeoutException e) {
                    if (attemptsCount == 2) {
                        return new Response(Language.getBundle().getString("noResponse"), 1);
                    }
                    attemptsCount++;
                    continue;
                }

                Response serverResponse = (Response) ObjectSerializer.deserializeObject(inputPacket.getData());
                String responseDefinition = serverResponse.getDefinition();
                int code = serverResponse.getCode();

                if (request.getCommandName().equals("login") && code == 0)
                    CommandManager.initCommands(responseDefinition);
                return serverResponse;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Response(Language.getBundle().getString("applicationError"), 100);
        }
        return null;
    }
}