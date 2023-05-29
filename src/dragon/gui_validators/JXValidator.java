package dragon.gui_validators;

import java.util.InputMismatchException;

public class JXValidator {

    public static boolean isValid(String x) {
        if (x.isBlank())
            return false;
        try {
            return (-300 <= Float.parseFloat(x) && Float.parseFloat(x) <= 300);
        } catch (InputMismatchException e) {
            return false;
        }
    }
}
