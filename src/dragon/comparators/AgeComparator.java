package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class AgeComparator implements Comparator<Dragon> {

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return (int) (o1.getAge() - o2.getAge());
    }

    @Override
    public String toString() {
        return Language.getProperty("age");
    }
}
