package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ChatConfig {
    @ConfKey("clan")
    @ConfDefault.DefaultString("&6Clan> {player} : &f{message}")
    @ConfComments("The Clans message format")
    TextComponent clan();

    @ConfKey("ally")
    @ConfDefault.DefaultString("&dAlly> [{clan}]{player} : &f{message}")
    @ConfComments("The Clans message format")
    TextComponent ally();
}
