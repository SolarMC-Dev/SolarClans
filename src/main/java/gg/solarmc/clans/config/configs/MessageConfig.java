package gg.solarmc.clans.config.configs;

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

    @ConfKey("chatToggled")
    @ConfComments("When a Player Toggles their Chat")
    @ConfDefault.DefaultString("You chat has been Toggled to {chatmode}")
    TextComponent chatToggled();

    @ConfKey("clanNotExist")
    @ConfComments("When a clan is not found or it doesn't exist")
    @ConfDefault.DefaultString("Clan does not exist!")
    TextComponent clanNotExist();

    @ConfKey("notInCLan")
    @ConfComments("When a player is not in a Clan")
    @ConfDefault.DefaultString("""
            &cYou are not in a Clan!
            Join a clan using /clan join [ClanName] if you are invited
            Create a clan using /clan create [ClanName]""")
    TextComponent notInClan();

    @ConfKey("leaderCommand")
    @ConfComments("When a normal player uses Leader Command")
    @ConfDefault.DefaultString("Only Clan Leader can use this Command")
    TextComponent leaderCommand();

    @ConfKey("playerOffline")
    @ConfComments("When a player is offline")
    @ConfDefault.DefaultString("&cThe player is offline")
    TextComponent playerOffline();

    @ConfKey("playerNotFound")
    @ConfComments("When a player is not found")
    @ConfDefault.DefaultString("&cCannot find the player!")
    TextComponent playerNotFound();

    @ConfKey("confirmMsg")
    @ConfComments("Confirm Messages for create,disband,etc")
    @ConfDefault.DefaultString("&eConfirm Message : Use &6{command} &eto {action}")
    TextComponent confirmMsg();

    @ConfKey("ally.add")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection AllyAddConfig allyAdd();

    @ConfKey("ally.remove")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection AllyRemoveConfig allyRemove();

    @ConfKey("clan.create")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanCreateConfig clanCreate();

    @ConfKey("clan.disband.disbanded")
    @ConfComments("When a player Disbands the Clan")
    @ConfDefault.DefaultString("{player} disbanded the clan!")
    TextComponent clanDisbanded();

    @ConfKey("clan.info")
    @ConfComments("The Information sent :)")
    @ConfDefault.DefaultString("""
            Information about {clan}
            Kills: {kills}
            Assists: {assists}
            Deaths: {deaths}
            Ally: {ally}
            Members: {members}""")
    TextComponent clanInfo();

    @ConfKey("clan.invite")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanInviteConfig clanInvite();

    @ConfKey("clan.join")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanJoinConfig clanJoin();

    @ConfKey("clan.kick")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanKickConfig clanKick();

    @ConfKey("clan.leave")
    @ConfComments("When a player leaves the clan")
    @ConfDefault.DefaultString("&e{player} left the clan.")
    TextComponent clanLeave();

    @ConfKey("clan.pvp")
    @ConfComments("When the leader sets the pvp to on/off")
    @ConfDefault.DefaultString("&aPVP has been toggled to &6{mode}")
    TextComponent clanPVP();

    @ConfKey("clan.rename")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanRenameConfig clanRename();

    @ConfKey("clan.setLeader")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanSetLeaderConfig clanSetLeader();

    @ConfKey("clan.top")
    @ConfDefault.DefaultObject("defaultConfig")
    @SubSection ClanTopConfig clanTop();

    @ConfKey("chatFormat.clan")
    @ConfDefault.DefaultString("&6Clan> {player} : &f{message}")
    @ConfComments("The Clans message format")
    TextComponent clanChatFormat();

    @ConfKey("chatFormat.ally")
    @ConfDefault.DefaultString("&dAlly> [{clan}]{player} : &f{message}")
    @ConfComments("The Clans message format")
    TextComponent allyChatFormat();


}
