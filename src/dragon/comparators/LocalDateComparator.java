package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class LocalDateComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        if (o1.getCreationDate().isBefore(o2.getCreationDate()))
            return -1;
        else if (o1.getCreationDate().equals(o2.getCreationDate()))
            return 0;
        else return 1;
    }

    @Override
    public String toString() {
        return Language.getProperty("date");
    }
}
