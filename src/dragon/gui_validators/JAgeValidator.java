package dragon.gui_validators;

import java.util.InputMismatchException;

public class JAgeValidator {

    public static boolean isValid(String age) {
        if (age.isBlank())
            return false;
        try {
            long value = Long.parseLong(age);
            return value > 0;
        } catch (InputMismatchException e) {
            return false;
        }
    }
}
