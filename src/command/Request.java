package command;

import java.io.Serializable;

public final class Request implements Serializable {

    private final String commandName;
    private final byte[] serializedArgument;

    public Request(String commandName, byte[] serializedArgument) {
        this.commandName = commandName;
        this.serializedArgument = serializedArgument;
    }

    public String getCommandName() {
        return commandName;
    }

    public byte[] getSerializedArgument() {
        return serializedArgument;
    }

}
