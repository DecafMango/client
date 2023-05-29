package dragon.comparators;

import dragon.Dragon;
import dragon.DragonCharacter;
import dragon.DragonType;
import gui.Language;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DragonCharacterComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        Map<DragonCharacter, Integer> cost = new HashMap<>();
        cost.put(DragonCharacter.NULL, 1);
        cost.put(DragonCharacter.CHAOTIC_EVIL, 2);
        cost.put(DragonCharacter.GOOD, 3);
        cost.put(DragonCharacter.WISE, 4);

        return cost.get(o1.getCharacter()) - cost.get(o2.getCharacter());
    }

    @Override
    public String toString() {
        return Language.getProperty("character");
    }
}
