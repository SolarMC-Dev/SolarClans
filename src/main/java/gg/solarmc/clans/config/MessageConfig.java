package gg.solarmc.clans.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface MessageConfig {
    @ConfKey("error")
    @ConfComments("When some error occurs")
    @ConfDefault.DefaultString("Something went wrong, Please try again Later!!")
    String error();

    @ConfKey("chatToggled")
    @ConfComments("When a Player Toggles their Chat")
    @ConfDefault.DefaultString("You chat has been Toggled to {chatmode}")
    String chatToggled();

    @ConfKey("clanNotExist")
    @ConfComments("When a clan is not found or it doesn't exist")
    @ConfDefault.DefaultString("Clan does not exist!")
    String clanNotExist();

    @ConfKey("leaderCommand")
    @ConfComments("When a normal player uses Leader Command")
    @ConfDefault.DefaultString("Only Clan Leader can use this Command")
    String leaderCommand();

    @ConfKey("ally.allied")
    @ConfComments("When a Clan is allied to another")
    @ConfDefault.DefaultString("You have been allied to {clan}")
    String allyAllied();

    @ConfKey("ally.revoked")
    @ConfComments("When a Clan removes their ally")
    @ConfDefault.DefaultString("You revoked your ally")
    String allyRevoked();

    @ConfKey("clan.create")
    @ConfComments("When a Player Creates a Clan")
    @ConfDefault.DefaultString("Your clan has been Created!")
    String clanCreated();

    @ConfKey("clan.disband")
    @ConfComments("When a player Disbands the Clan")
    @ConfDefault.DefaultString("{player} disbanded the clan!")
    String clanDisbanded();
}
