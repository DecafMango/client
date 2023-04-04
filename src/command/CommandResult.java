package command;

import java.io.Serializable;

public final class CommandResult implements Serializable {

    private final String definition;

    public CommandResult(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }
}
