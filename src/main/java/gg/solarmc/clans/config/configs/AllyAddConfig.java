package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface AllyAddConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name which they want to ally")
    @ConfDefault.DefaultString("&cYou have to specify the name of the Clan you have to ally!!")
    TextComponent invalidArgs();

    @ConfKey("allyPresent")
    @ConfComments("When the clan is already allied to other clan")
    @ConfDefault.DefaultString("&cYou already have a ally clan\n"+
            "&You can use /ally remove to remove your current ally")
    TextComponent allyPresent();

    @ConfKey("onGoingRequest")
    @ConfComments("When a clan already has a ongoing ally request to other clan")
    @ConfDefault.DefaultString("&cYou already have ongoing request to {clan}")
    TextComponent onGoingRequest();

    @ConfKey("clanHasAlly")
    @ConfComments("When the requested clan already has a ally ")
    @ConfDefault.DefaultString("The Clan {clan} already has an ally clan")
    TextComponent clanHasALly();

    @ConfKey("allied")
    @ConfComments("When a Clan is allied to another")
    @ConfDefault.DefaultString("&aYou have been allied to {clan}")
    TextComponent allied();

    @ConfKey("request")
    @ConfComments("When a clan requests ally")
    @ConfDefault.DefaultString("&a{sender} has requested a Clan Ally to {clan}")
    TextComponent request();

}
