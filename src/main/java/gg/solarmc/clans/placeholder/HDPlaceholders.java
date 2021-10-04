package gg.solarmc.clans.placeholder;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.arim.omnibus.util.concurrent.CentralisedFuture;

import java.util.List;

// Holographic Displays Placeholders
public class HDPlaceholders {
    private final Logger LOGGER = LoggerFactory.getLogger(HDPlaceholders.class);
    private final SolarClans plugin;

    public HDPlaceholders(SolarClans plugin) {
        this.plugin = plugin;
    }

    public void registerPlaceHolders() {
        for (int i = 1; i <= 10; i++) {
            int index = i - 1;
            getPlaceHolder(index).thenAccept(string ->
                    HologramsAPI.registerPlaceholder(plugin, "topclan_" + index + 1, 1, () -> string)
            );
        }
    }

    private CentralisedFuture<String> getPlaceHolder(int i) {
        DataCenter dataCenter = plugin.getServer().getDataCenter();
        return dataCenter.transact(transaction -> {
            List<ClanManager.TopClanResult> results = dataCenter.getDataManager(ClansKey.INSTANCE)
                    .getTopClanKills(transaction, 10);

            if (results.size() <= i) return ("???");

            ClanManager.TopClanResult clan = results.get(i);
            return clan.clanName() + " - " + clan.statisticValue();
        }).exceptionally(e -> {
            LOGGER.error("Failed to get Top clan kills", e);
            return null;
        });
    }

}
