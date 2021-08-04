package gg.solarmc.clans;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
            HologramsAPI.registerPlaceholder(plugin, "topclan_" + i, 1, () -> getPlaceHolder(index));
        }
    }

    private String getPlaceHolder(int i) {
        DataCenter dataCenter = plugin.getServer().getDataCenter();
        AtomicReference<String> value = new AtomicReference<>();
        dataCenter.runTransact(transaction -> {
            List<ClanManager.TopClanResult> results = dataCenter.getDataManager(ClansKey.INSTANCE).getTopClanKills(transaction, 10);

            if (results.size() <= i) value.set("???");

            final ClanManager.TopClanResult clan = results.get(i);
            value.set(clan.clanName() + " - " + clan.statisticValue());
        }).exceptionally(e -> {
            LOGGER.error("Failed to get Top clan kills", e);
            return null;
        });

        return value.get();
    }

}
