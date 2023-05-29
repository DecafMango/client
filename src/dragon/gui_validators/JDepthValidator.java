package dragon.gui_validators;

import java.util.InputMismatchException;

public class JDepthValidator {

    public static boolean isValid(String depth) {
        if (depth.isBlank())
            return true;
        try {
            Integer.parseInt(depth);
            return true;
        } catch (InputMismatchException e) {
            return false;
        }
    }
}
