package gg.solarmc.clans.config.sorter;

import space.arim.dazzleconf.sorter.ConfigurationSorter;
import space.arim.dazzleconf.sorter.SortableConfigurationEntry;

public class MethodNameBasedSorter implements ConfigurationSorter {
    @Override
    public int compare(SortableConfigurationEntry o1, SortableConfigurationEntry o2) {
        if (o1.getComments().isEmpty() && o2.getComments().isEmpty())
            return o1.getKey().compareTo(o2.getKey());

        if (o1.getComments().isEmpty()) return -1;
        if (o2.getComments().isEmpty()) return 1;

        return o1.getKey().compareTo(o2.getKey());
    }
}
