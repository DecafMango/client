package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class NameComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return o1.getName().length() - o2.getName().length();
    }

    @Override
    public String toString() {
        return Language.getProperty("name");
    }
}
