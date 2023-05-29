package command;

import java.io.Serializable;

public final class Response implements Serializable {

    private final String definition;
    private final int code;

    public Response(String definition, int code) {
        this.definition = definition;
        this.code = code;
    }

    public String getDefinition() {
        return definition;
    }

    public int getCode() {
        return code;
    }
}
