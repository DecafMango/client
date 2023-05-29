package dragon.gui_validators;

public class JNameValidator {

    public static boolean isValid(String name) {
        return !name.isBlank();
    }
}
