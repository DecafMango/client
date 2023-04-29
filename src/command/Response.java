package command;

import java.io.Serializable;

public final class Response implements Serializable {

    private final String definition;

    public Response(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }
}
