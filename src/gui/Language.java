package gui;

import collection.CollectionManager;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class Language {

    private static String language;
    private static ResourceBundle resourceBundle;

    static {
        language = "english";
        resourceBundle = ResourceBundle.getBundle("resources_US", new Locale("US", "US"));
    }
    
    public static String getProperty(String key) {
        if (resourceBundle.getLocale().getCountry().equals("RU")) {
            return new String(resourceBundle.getString(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        return resourceBundle.getString(key);
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        Language.language = language;
        switch (language) {
            case "english":
                resourceBundle = ResourceBundle.getBundle("resources_US", new Locale("US", "US"));
                break;
            case "íslenskur":
                resourceBundle = ResourceBundle.getBundle("resources_IS", new Locale("IS", "IS"));
                break;
            case "shqiptare":
                resourceBundle = ResourceBundle.getBundle("resources_AL", new Locale("AL", "AL"));
                break;
            case "русский":
                resourceBundle = ResourceBundle.getBundle("resources_RU", new Locale("RU", "RU"));
        }
        StartWindow.changeLanguage();
        BasicWindow.changeLanguage();
        CollectionManager.updateCollection();
        CommandWindow.changeLanguage();
        ArgumentWindow.changeLanguage();
        CreationWindow.changeLanguage();
    }

    public static void setStartLanguage(String language) {
        Language.language = language;
        switch (language) {
            case "english":
                resourceBundle = ResourceBundle.getBundle("resources_US", new Locale("US", "US"));
                break;
            case "íslenskur":
                resourceBundle = ResourceBundle.getBundle("resources_IS", new Locale("IS", "IS"));
                break;
            case "shqiptare":
                resourceBundle = ResourceBundle.getBundle("resources_AL", new Locale("AL", "AL"));
                break;
            case "русский":
                resourceBundle = ResourceBundle.getBundle("resources_RU", new Locale("RU", "RU"));
        }
        StartWindow.changeLanguage();
    }

    public static ResourceBundle getBundle() {
        return resourceBundle;
    }
}
