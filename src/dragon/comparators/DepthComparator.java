package dragon.comparators;

import dragon.Dragon;
import gui.Language;

import java.util.Comparator;

public class DepthComparator implements Comparator<Dragon> {
    @Override
    public int compare(Dragon o1, Dragon o2) {
        if (o1.getCave() != null && o2.getCave() != null)
            return o1.getCave().getDepth() - o2.getCave().getDepth();
        else if (o1.getCave() != null)
            return -1;
        else if (o2.getCave() != null)
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return Language.getProperty("color");
    }
}
