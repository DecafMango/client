package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class YComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return o1.getCoordinates().getY() - o2.getCoordinates().getY();
    }

    @Override
    public String toString() {
        return Language.getProperty("y");
    }
}
