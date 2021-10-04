package gg.solarmc.clans.config.configs.clan;

import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanKickConfig {

    @ConfKey("invalid-args")
    @ConfComments("When player doesn't specify 1st arg ie. player's name where he wants to kick ")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Player you want to Kick!!")
    TextComponent invalidArgs();

    @ConfKey("player-absent")
    @ConfComments("When the player is not in his clan")
    @ConfDefault.DefaultString("&cPlayer is not in your clan!!")
    TextComponent playerAbsent();

    @ConfKey("leader-kick")
    @ConfComments("When the leader tries to kick himself")
    @ConfDefault.DefaultString("&cYou can't kick yourself!")
    TextComponent leaderKick();

    @ConfKey("kicked")
    @ConfComments("When the player is kicked")
    @ConfDefault.DefaultString("&e{player} kick {playerKicked} from the clan")
    TextComponent kicked();

}
