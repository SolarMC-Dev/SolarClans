package gg.solarmc.clans.config.configs.clan;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanSetLeaderConfig {

    @ConfKey("invalid-args")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Player you want to Invite!!")
    TextComponent invalidArgs();

    @ConfKey("player-absent")
    @ConfComments("When the player is not in the clan")
    @ConfDefault.DefaultString("&cThe player should be in your Clan!!")
    TextComponent playerAbsent();

    @ConfKey("set-leader")
    @ConfComments("When the leader is set")
    @ConfDefault.DefaultString("{player} transferred the clan to &6{newPlayer}")
    TextComponent setLeader();

}
