package gg.solarmc.clans.config.configs;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanInviteConfig {

    @ConfKey("invalidArgs")
    @ConfComments("When player doesn't specify 1st arg ie. Player's name who he is inviting")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Player you want to Invite!!")
    TextComponent invalidArgs();

    @ConfKey("maxPlayersReached")
    @ConfComments("When the clan's member capacity is full ie. 5 members")
    @ConfDefault.DefaultString("&cThere are already 5 players in the Clan!! You cannot invite more people!\n" +
            "&eYou can kick players by /clan kick command")
    TextComponent maxPlayersReached();

    @ConfKey("playerPresent")
    @ConfComments("When the player invited is already present in the clan")
    @ConfDefault.DefaultString("&ePlayer is already in your Clan!")
    TextComponent playerPresent();

    @ConfKey("playerPresentInClan")
    @ConfComments("When the player is already present in another Clan!")
    @ConfDefault.DefaultString("&cPlayer is already in other Clan!")
    TextComponent playerPresentInClan();

    @ConfKey("playerInvited")
    @ConfComments("When a player is invited")
    @ConfDefault.DefaultString("&e{player} invited {playerInvited} to the clan")
    TextComponent playerInvited();

}
