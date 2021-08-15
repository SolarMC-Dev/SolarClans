package gg.solarmc.clans.placeholder;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ClanNamePlaceholder extends PlaceholderExpansion {
    private final SolarClans plugin;

    public ClanNamePlaceholder(SolarClans plugin) {
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
        return "clan_relation";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (!player.isOnline())
            return "";
        Clan clan = player.getPlayer().getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        return clan == null ? "" : clan.currentClanName();
    }
}
