package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import org.checkerframework.common.value.qual.StringVal;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanJoinConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's name where he wants to join")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Clan you want to Join!!")
    TextComponent invalidArgs();


    @ConfKey("notInvited")
    @ConfComments("When the player is not invited and he tries to join the clan")
    @ConfDefault.DefaultString("You are not invited to this clan!!")
    TextComponent notInvited();

    @ConfKey("joined")
    @ConfComments("When the player joins the clan")
    @ConfDefault.DefaultString("&a{player} joined the Clan")
    TextComponent joined();

}
