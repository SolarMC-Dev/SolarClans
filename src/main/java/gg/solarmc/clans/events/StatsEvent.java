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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsEvent implements Listener {
    private final Map<UUID, UUID> assists = new HashMap<>();

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

        Player assister = server.getPlayer(assists.get(player.getUniqueId()));

        server.getDataCenter().runTransact(transaction -> {
            Clan playerClan = getClan(player);
            Clan killerClan = getClan(killer);
            Clan assisterClan = getClan(assister);

            if (playerClan != null) playerClan.addDeaths(transaction, 1);
            if (killerClan != null) killerClan.addKills(transaction, 1);
            if (assisterClan != null) assisterClan.addAssists(transaction, 1);
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
