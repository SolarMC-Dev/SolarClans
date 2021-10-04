package gg.solarmc.clans.config.configs.ally;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface AllyRemoveConfig {

    @ConfKey("no-ally")
    @ConfComments("When the clan doesn't have ally")
    @ConfDefault.DefaultString("&cYou don't have a ally!!")
    TextComponent noAlly();

    @ConfKey("revoked")
    @ConfComments("When a Clan removes their ally")
    @ConfDefault.DefaultString("&eYou revoked your ally")
    TextComponent revoked();
}
