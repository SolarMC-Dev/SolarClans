package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanSetLeaderConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Player you want to Invite!!")
    TextComponent invalidArgs();

    @ConfKey("playerAbsent")
    @ConfComments("When the player is not in the clan")
    @ConfDefault.DefaultString("&cThe player should be in your Clan!!")
    TextComponent playerAbsent();

    @ConfKey("setLeader")
    @ConfComments("When the leader is set")
    @ConfDefault.DefaultString("{player} transferred the clan to &6{newPlayer}")
    TextComponent setLeader();

}
