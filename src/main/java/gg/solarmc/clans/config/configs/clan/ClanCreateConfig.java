package gg.solarmc.clans.config.configs.clan;

import com.drtshock.playervaults.config.annotation.Comment;
import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

public interface ClanCreateConfig {

    @ConfKey("clan-present")
    @ConfComments("When the player is already in a clan")
    @ConfDefault.DefaultString("&cYou are already in a Clan!!")
    TextComponent clanPresent();

    @ConfKey("invalid-args")
    @ConfComments("When player doesn't specify 1st arg ie. Clan's Name")
    @ConfDefault.DefaultString("&cYou need to specify the Name of the Clan!!")
    TextComponent invalidArgs();

    @ConfKey("created")
    @ConfComments("When a Player Creates a Clan")
    @ConfDefault.DefaultString("Your clan has been Created!")
    TextComponent created();

    @ConfKey("not-enough-money")
    @ConfComments("When a player doesn't have enough money to create the clan")
    @ConfDefault.DefaultString("&cYou do not have enough money to Create a Clan!!")
    TextComponent notEnoughMoney();

    @ConfKey("clan-name-present")
    @ConfComments("When a clan name is already present")
    @ConfDefault.DefaultString("&cClan already exists!!")
    TextComponent clanNamePresent();
}
