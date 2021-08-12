package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanRenameConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Clan!!")
    TextComponent invalidArgs();

    @ConfKey("renamed")
    @ConfComments("When the player renames the clan")
    @ConfDefault.DefaultString("&a{player} renamed the clan to &6{name}")
    TextComponent renamed();

}
