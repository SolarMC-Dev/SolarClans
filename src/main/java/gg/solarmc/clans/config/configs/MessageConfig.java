package gg.solarmc.clans.config.configs;

import gg.solarmc.clans.config.configs.ally.AllyConfig;
import gg.solarmc.clans.config.configs.clan.ClanConfig;
import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

public interface MessageConfig {
    @ConfKey("error")
    @ConfComments("When some error occurs")
    @ConfDefault.DefaultString("Something went wrong, Please try again Later!!")
    TextComponent error();

    @ConfKey("chat-toggled")
    @ConfComments("When a Player Toggles their Chat")
    @ConfDefault.DefaultString("You chat has been Toggled to {chatmode}")
    TextComponent chatToggled();

    @ConfKey("clan-not-exist")
    @ConfComments("When a clan is not found or it doesn't exist")
    @ConfDefault.DefaultString("Clan does not exist!")
    TextComponent clanNotExist();

    @ConfKey("not-in-clan")
    @ConfComments("When a player is not in a Clan")
    @ConfDefault.DefaultString("""
            &cYou are not in a Clan!
            Join a clan using /clan join [ClanName] if you are invited
            Create a clan using /clan create [ClanName]""")
    TextComponent notInClan();

    @ConfKey("leader-command")
    @ConfComments("When a normal player uses Leader Command")
    @ConfDefault.DefaultString("Only Clan Leader can use this Command")
    TextComponent leaderCommand();

    @ConfKey("player-offline")
    @ConfComments("When a player is offline")
    @ConfDefault.DefaultString("&cThe player is offline")
    TextComponent playerOffline();

    @ConfKey("player-not-found")
    @ConfComments("When a player is not found")
    @ConfDefault.DefaultString("&cCannot find the player!")
    TextComponent playerNotFound();

    @ConfKey("confirm-message")
    @ConfComments("Confirm Messages for create,disband,etc")
    @ConfDefault.DefaultString("&eConfirm Message : Use &6{command} &eto {action}")
    TextComponent confirmMsg();

    @ConfKey("ally")
    @SubSection AllyConfig ally();

    @ConfKey("clan")
    @SubSection ClanConfig clan();

    @ConfKey("chat-format")
    @SubSection ChatConfig chatFormat();
}
