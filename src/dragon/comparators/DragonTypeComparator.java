package dragon.comparators;

import dragon.Color;
import dragon.Dragon;
import dragon.DragonType;
import gui.Language;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DragonTypeComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        Map<DragonType, Integer> cost = new HashMap<>();
        cost.put(DragonType.FIRE, 1);
        cost.put(DragonType.AIR, 2);
        cost.put(DragonType.WATER, 3);
        cost.put(DragonType.UNDERGROUND, 4);

        return cost.get(o1.getType()) - cost.get(o2.getType());
    }

    @Override
    public String toString() {
        return Language.getProperty("type");
    }
}
