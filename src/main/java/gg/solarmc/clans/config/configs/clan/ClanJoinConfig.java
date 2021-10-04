package gg.solarmc.clans.config.configs.clan;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanJoinConfig {

    @ConfKey("invalid-args")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's name where he wants to join")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Clan you want to Join!!")
    TextComponent invalidArgs();

    @ConfKey("join-request-sent")
    @ConfComments("When the player is not invited so a join request is sent (Message for that player)")
    @ConfDefault.DefaultString("You sent a join request to this clan!")
    TextComponent joinRequestSent();

    @ConfKey("join-request")
    @ConfComments("When the player is not invited so a join request is sent (Message for leader of that clan)")
    @ConfDefault.DefaultString("&e{player} sent a join request")
    TextComponent joinRequest();

    @ConfKey("joined")
    @ConfComments("When the player joins the clan")
    @ConfDefault.DefaultString("&a{player} joined the Clan")
    TextComponent joined();
}
