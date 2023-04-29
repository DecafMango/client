package command;

import java.io.Serializable;

public final class Request implements Serializable {

    private final String login;
    private final String commandName;
    private final byte[] serializedArgument;

    public Request(String login, String commandName, byte[] serializedArgument) {
        this.login = login;
        this.commandName = commandName;
        this.serializedArgument = serializedArgument;
    }

    public String getLogin() {
        return login;
    }

    public String getCommandName() {
        return commandName;
    }

    public byte[] getSerializedArgument() {
        return serializedArgument;
    }

}
