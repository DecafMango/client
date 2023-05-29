package dragon.gui_validators;

import java.util.InputMismatchException;

public class JYValidator {

    public static boolean isValid(String y) {
        if (y.isBlank())
            return false;
        try {
            return (-250 <= Integer.parseInt(y) && Integer.parseInt(y) <= 250);
        } catch (InputMismatchException e) {
            return false;
        }
    }
}
