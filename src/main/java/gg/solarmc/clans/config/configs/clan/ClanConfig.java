package gg.solarmc.clans.config.configs.clan;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

public interface ClanConfig {
    @ConfKey("create")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanCreateConfig create();

    @ConfKey("disband")
    @ConfComments("When a player Disbands the Clan")
    @ConfDefault.DefaultString("{player} disbanded the clan!")
    TextComponent disband();

    @ConfKey("info")
    @ConfComments("The Information sent :)")
    @ConfDefault.DefaultString("""
            Information about {clan}
            Kills: {kills}
            Assists: {assists}
            Deaths: {deaths}
            Ally: {ally}
            Members: {members}""")
    TextComponent info();

    @ConfKey("invite")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanInviteConfig invite();

    @ConfKey("join")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanJoinConfig join();

    @ConfKey("kick")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanKickConfig kick();

    @ConfKey("leave")
    @ConfComments("When a player leaves the clan")
    @ConfDefault.DefaultString("&e{player} left the clan.")
    TextComponent leave();

    @ConfKey("pvp")
    @ConfComments("When the leader sets the pvp to on/off")
    @ConfDefault.DefaultString("&aPVP has been toggled to &6{mode}")
    TextComponent pvp();

    @ConfKey("rename")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanRenameConfig rename();

    @ConfKey("setLeader")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanSetLeaderConfig setLeader();

    @ConfKey("top")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanTopConfig top();
}
