package gg.solarmc.clans.config.configs.ally;

import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

public interface AllyConfig {

    @ConfKey("add")
    @SubSection AllyAddConfig add();

    @ConfKey("remove")
    @SubSection AllyRemoveConfig remove();

}
