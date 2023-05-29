package collection;

import client.Client;
import command.CommandManager;
import command.Request;
import command.Response;
import dragon.*;
import dragon.comparators.IdComparator;
import gui.BasicWindow;
import gui.DragonsWindow;
import gui.Language;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CollectionManager {

    private static List<Dragon> collection;
    private static Comparator<Dragon> comparator;
    private static DateTimeFormatter US;
    private static DateTimeFormatter IS;
    private static DateTimeFormatter AL;
    private static DateTimeFormatter RU;
    private static volatile LocalTime timePointer;
    private static UpdateThread timer;

    static {
        comparator = new IdComparator();
        US = DateTimeFormatter.ofPattern("MM.dd.YY");
        IS = DateTimeFormatter.ofPattern("dd-MM-YY");
        AL = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        RU = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        startTimer();
    }

    public static Response updateCollection() {
        collection = new ArrayList<>();
        Request request = new Request(CommandManager.getLogin(), "collection", null, Language.getLanguage());
        Response response = Client.workWithServer(request);

        String[] dragons = response.getDefinition().split(";");
        String[][] data = new String[dragons.length][10];

        if (dragons.length == 1 && dragons[0].isBlank())
            data = new String[][] {{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}};
        else {
            for (int i = 0; i < dragons.length; i++) {
                String[] dragonCharacteristics = dragons[i].split(",");
                int id = Integer.parseInt(dragonCharacteristics[0]);
                String name = dragonCharacteristics[1];
                float x = Float.parseFloat(dragonCharacteristics[2]);
                int y = Integer.parseInt(dragonCharacteristics[3]);
                Coordinates coordinates = new Coordinates(x, y);
                LocalDate creationDate = LocalDate.parse(dragonCharacteristics[4]);
                Long age = Long.parseLong(dragonCharacteristics[5]);
                Color color = Color.valueOf(dragonCharacteristics[6]);
                DragonType type = DragonType.valueOf(dragonCharacteristics[7]);
                DragonCharacter character = DragonCharacter.valueOf(dragonCharacteristics[8]);
                String depth = dragonCharacteristics[9];
                DragonCave cave = depth.equals("null") ? null : new DragonCave(Integer.parseInt(depth));
                String owner = dragonCharacteristics[10];
                Dragon dragon = new Dragon(id, name, coordinates, creationDate, age, color,
                        type, character, cave, owner);
                collection.add(dragon);
                DragonsWindow.paintDragon(dragon);
            }

            collection = collection.stream().sorted(comparator).toList();
            for (int i = 0; i < dragons.length; i++) {
                data[i] = collection.get(i).toString().split(",");
                switch (Language.getLanguage()) {
                    case "english":
                        data[i][4] = collection.get(i).getCreationDate().format(US);
                        break;
                    case "íslenskur":
                        data[i][4] = collection.get(i).getCreationDate().format(IS);
                        break;
                    case "shqiptare":
                        data[i][4] = collection.get(i).getCreationDate().format(AL);
                        break;
                    case "русский":
                        data[i][4] = collection.get(i).getCreationDate().format(RU);

                }
            }
        }

        BasicWindow.updateData(data);
        timePointer = LocalTime.now();
        return response;
    }

    public static void setComparator (Comparator<Dragon> comparator) {
        CollectionManager.comparator = comparator;
    }

    public static List<Dragon> getCollection() {
        return collection;
    }

    public static void startTimer() {
        timePointer = LocalTime.now();
        timer = new UpdateThread();
        timer.start();
    }

    public static void stopTimer() {
        timer.stopTimer();
    }

    private static class UpdateThread extends Thread {

        private boolean isWorking = true;
        @Override
        public void run() {
            while (isWorking) {
                Duration duration = Duration.between(timePointer, LocalTime.now());
                if (duration.toSeconds() >= 10) {
                    updateCollection();
                    timePointer = LocalTime.now();
                }
            }
        }

        public void stopTimer() {
            isWorking = false;
        }
    }
}
