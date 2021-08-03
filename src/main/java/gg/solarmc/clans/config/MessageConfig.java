package gg.solarmc.clans.config;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface MessageConfig {
    @ConfKey("error")
    @ConfComments("When some error occurs")
    @ConfDefault.DefaultString("Something went wrong, Please try again Later!!")
    TextComponent error();

    @ConfKey("chatToggled")
    @ConfComments("When a Player Toggles their Chat")
    @ConfDefault.DefaultString("You chat has been Toggled to {chatmode}")
    TextComponent chatToggled();

    @ConfKey("clanNotExist")
    @ConfComments("When a clan is not found or it doesn't exist")
    @ConfDefault.DefaultString("Clan does not exist!")
    TextComponent clanNotExist();

    @ConfKey("leaderCommand")
    @ConfComments("When a normal player uses Leader Command")
    @ConfDefault.DefaultString("Only Clan Leader can use this Command")
    TextComponent leaderCommand();

    @ConfKey("ally.allied")
    @ConfComments("When a Clan is allied to another")
    @ConfDefault.DefaultString("You have been allied to {clan}")
    TextComponent allyAllied();

    @ConfKey("ally.revoked")
    @ConfComments("When a Clan removes their ally")
    @ConfDefault.DefaultString("You revoked your ally")
    TextComponent allyRevoked();

    @ConfKey("clan.create")
    @ConfComments("When a Player Creates a Clan")
    @ConfDefault.DefaultString("Your clan has been Created!")
    TextComponent clanCreated();

    @ConfKey("clan.disband")
    @ConfComments("When a player Disbands the Clan")
    @ConfDefault.DefaultString("{player} disbanded the clan!")
    TextComponent clanDisbanded();
}
