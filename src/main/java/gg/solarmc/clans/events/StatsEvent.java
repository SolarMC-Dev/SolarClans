package gg.solarmc.clans.events;

import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsEvent implements Listener {
    private final Map<UUID, UUID> assists = new HashMap<>();
    private final Logger LOGGER = LoggerFactory.getLogger(StatsEvent.class);

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager)
            assists.put(damaged.getUniqueId(), damager.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Server server = player.getServer();

        UUID assisterUUID = assists.get(player.getUniqueId());
        Player assister = null;
        if (assisterUUID != null) assister = server.getPlayer(assisterUUID);

        Player finalAssister = assister;
        server.getDataCenter().runTransact(transaction -> {
            Clan playerClan = getClan(player);
            Clan killerClan = getClan(killer);
            Clan assisterClan = getClan(finalAssister);

            if (playerClan != null) playerClan.addDeaths(transaction, 1);
            if (killerClan != null) killerClan.addKills(transaction, 1);
            if (assisterClan != null && killer.getUniqueId() != assisterUUID) assisterClan.addAssists(transaction, 1);
        }).exceptionally(e -> {
            LOGGER.error("Cannot either add death or add kills or add assist");
            return null;
        });
    }

    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player)
            if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                assists.remove(player.getUniqueId());
    }

    private Clan getClan(Player player) {
        if (player == null) return null;
        return player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
    }
}
