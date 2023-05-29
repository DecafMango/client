package command;

import java.io.Serializable;

public final class Request implements Serializable {

    private final String login;
    private final String commandName;
    private final byte[] serializedArgument;
    private final String language;

    public Request(String login, String commandName, byte[] serializedArgument, String language) {
        this.login = login;
        this.commandName = commandName;
        this.serializedArgument = serializedArgument;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }
}
