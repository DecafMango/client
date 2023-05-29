package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class XComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return (int) (o1.getCoordinates().getX() - o2.getCoordinates().getX());
    }

    @Override
    public String toString() {
        return Language.getProperty("x");
    }
}
