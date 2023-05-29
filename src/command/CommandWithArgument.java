package command;

public class CommandWithArgument extends Command {

    private final int argumentType;

    public CommandWithArgument(int argumentType) {
        this.argumentType = argumentType;
    }

    public int getArgumentType() {
        return argumentType;
    }

}
