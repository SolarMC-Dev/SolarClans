package gg.solarmc.clans.placeholder;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClansRelationColor extends PlaceholderExpansion implements Relational {
    private final SolarClans plugin;

    public ClansRelationColor(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clan_name_chat_relation";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, Player other, String identifier) {
        if (player == null || other == null)
            return null;

        Clan playerClan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        Clan otherClan = other.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (playerClan != null && otherClan != null) {
            if (playerClan.equals(otherClan))
                return ChatColor.GREEN.toString();
            else if (otherClan.equals(playerClan.currentAllyClan().orElse(null)))
                return ChatColor.LIGHT_PURPLE.toString();
            // TODO:
            // else if(otherClan.equals(playerClan.currentEnemyClan().orElse(null)))
            //  return ChatColor.RED.toString();
        }

        return ChatColor.WHITE.toString();
    }
}
