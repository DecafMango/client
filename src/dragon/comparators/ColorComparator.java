package dragon.comparators;

import dragon.Color;
import dragon.Dragon;
import gui.Language;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ColorComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        Map<Color, Integer> cost = new HashMap<>();
        cost.put(Color.BROWN, 1);
        cost.put(Color.BLACK, 2);
        cost.put(Color.WHITE, 3);

        return cost.get(o1.getColor()) - cost.get(o2.getColor());
    }

    @Override
    public String toString() {
        return Language.getProperty("color");
    }
}
