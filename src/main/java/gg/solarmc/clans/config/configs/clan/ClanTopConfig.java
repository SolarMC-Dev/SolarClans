package gg.solarmc.clans.config.configs.clan;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanTopConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name")
    @ConfDefault.DefaultString("&cSpecify - kills|assists|deaths|balance")
    TextComponent invalidArgs();

    @ConfKey("invalidOption")
    @ConfComments("When the player has put anything instead of kills|assists|deaths|balance")
    @ConfDefault.DefaultString("&cNot Valid Option use kills, assists, deaths or balance")
    TextComponent invalidOption();

    @ConfKey("leaderboard")
    @ConfComments("The leaderboard start")
    @ConfDefault.DefaultString("&6Leaderboard")
    TextComponent leaderboard();

    @ConfKey("clanFormat")
    @ConfComments("The leaderboard format")
    @ConfDefault.DefaultString("{number}. {clan} - {value}")
    TextComponent clanFormat();

}
