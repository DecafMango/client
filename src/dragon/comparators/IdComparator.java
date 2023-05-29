package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class IdComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return o1.getId() - o2.getId();
    }

    @Override
    public String toString() {
        return Language.getProperty("id");
    }
}
