package command.client_command;

import command.CommandManager;
import command.CommandWithArgument;
import command.CommandWithCreation;
import command.Request;
import dragon.Color;
import dragon.Coordinates;
import dragon.DragonCharacter;
import dragon.DragonType;
import dragon.validators.Validator;
import dragon.validators.ValidatorManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;


public final class ExecuteScript extends CommandWithArgument {

    private static List<String> filePaths = new ArrayList<>();
    private static List<String> filePathsStack = new ArrayList<>();
    private static boolean isStartedByScript = false;

    public ExecuteScript() {
        super(1);
    }

    public static Deque<Request> execute(String filepath) {

        if (CommandManager.isIsStartedHByScript())
            isStartedByScript = true;

        System.out.println("Выполнение скрипта №" + (filePathsStack.size() + 1));
        if (filePathsStack.contains(filepath)) {
            System.out.println("Команда execute_script не выполнится, чтобы не допустить рекурсии, так как " +
                    "производится повторный вызов файла " + filepath);
            return null;
        }
        filePathsStack.add(filepath);

        try {
            List<String> requests = new ArrayList<>();

            try (Reader reader = new FileReader(filepath);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {

                while (bufferedReader.ready()) {
                    String request = bufferedReader.readLine();
                    if (!request.isBlank())
                        requests.add(request);
                }
            }

            Deque<Request> validatedRequests = new ArrayDeque<>();

            for (String request : requests) {
                request = request.trim();
                while (request.contains("  "))
                    request = request.replace("  ", " ");
                String[] requestWords = request.split(" ");
                String commandName = requestWords[0];

                if (commandName.equals("execute_script")) {
                    if (requestWords.length == 2) {
                        Deque<Request> validatedRequests1 = execute(requestWords[1]);
                        if (validatedRequests1 != null)
                            validatedRequests.addAll(validatedRequests1);
                    } else {
                        System.out.println("Команда " + commandName + " требует наличия одного аргумента:\n" +
                                commandName + " <аргумент> (стрелки писать не нужно - это для акцентирования внимания.");
                    }
                } else if (CommandManager.getCommands().get(commandName) instanceof CommandWithCreation) {
                    if (requestWords.length == 2) {
                        Request requestWithDragon = createDragonFromScript(commandName, requestWords[1]);
                        if (requestWithDragon != null)
                            validatedRequests.add(requestWithDragon);
                    } else {
                        System.out.println("Команда " + commandName + " требует наличия одного аргумента:\n" +
                                commandName + " {\"character\":\"\",\"depth\":,\"color\":\"\",\"name\":\"\",\"coordinates\":[,],\"type\":\"\",\"age\":}");
                    }
                } else {
                    validatedRequests.add(CommandManager.validateRequest(request));
                }
            }

            filePathsStack.remove(filepath);
            if (validatedRequests.isEmpty() || validatedRequests == null) {
                System.out.println("Чтобы запустить приложение через скрипт - он должен содержать хотя бы одну команду.");
                System.exit(0);
            }
            return validatedRequests;
        } catch (IOException e) {
            e.printStackTrace();
            filePathsStack.remove(filepath);
            return null;
        }
    }

    private static Request createDragonFromScript(String commandName, String argument) {

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(argument);
            Map<String, Validator> fieldNames = ValidatorManager.getFieldAndValidator();

            Map<String, Object> dragonCharacteristics = new HashMap<>();

            for (String fieldName : fieldNames.keySet()) {
                Validator validator = fieldNames.get(fieldName);
                Object value = object.get(fieldName);

                switch (fieldName) {
                    case "coordinates":
                        if (ValidatorManager.getFieldAndValidator().get("coordinates").isValid(object.get(fieldName))) {
                            if (!fieldNames.get("x").isValid(((JSONArray) object.get(fieldName)).get(0)))
                                return null;
                            Double x = (Double) ((JSONArray) object.get(fieldName)).get(0);
                            if (!fieldNames.get("y").isValid(((JSONArray) object.get(fieldName)).get(1)))
                                return null;
                            Long y = (Long) ((JSONArray) object.get(fieldName)).get(1);
                            Coordinates coordinates = new Coordinates((float) x.doubleValue(), (int) y.longValue());
                            dragonCharacteristics.put(fieldName, coordinates);
                            continue;
                        } else
                            return null;
                    case "x":
                        continue;
                    case "y":
                        continue;
                    case "color":
                        if (validator.isValid(value))
                            dragonCharacteristics.put(fieldName, Color.valueOf((String) value));
                        else
                            return null;
                        continue;
                    case "type":
                        if (validator.isValid(value))
                            dragonCharacteristics.put(fieldName, DragonType.valueOf((String) value));
                        else
                            return null;
                        continue;
                    case "character":
                        if (validator.isValid(value))
                            dragonCharacteristics.put(fieldName, DragonCharacter.valueOf((String) value));
                        else
                            return null;
                        continue;
                    default:
                        if (validator.isValid(value))
                            dragonCharacteristics.put(fieldName, value);
                        else
                            return null;
                }
            }
            LocalDate creationDate = LocalDate.now();
            dragonCharacteristics.put("creationDate", creationDate);
            return new Request(commandName, ObjectSerializer.serializeObject(dragonCharacteristics));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
