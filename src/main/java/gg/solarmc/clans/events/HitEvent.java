package gg.solarmc.clans.events;

import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public record HitEvent(CommandHelper helper) implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !(event.getDamager() instanceof Player damager))
            return;

        // TODO: return , If global pvp is disabled at that location
        // TODO: Ally PVP

        Clan damagerClan = damager.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        Clan damagedPlayerClan = damagedPlayer.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (damagerClan == null || damagedPlayerClan == null) return;

        if (damagerClan.equals(damagedPlayerClan)) {
            if (!helper.isPvpOn(damagerClan)) {
                TextComponent msg = Component.text("(", NamedTextColor.WHITE, TextDecoration.BOLD)
                        .append(Component.text("!", NamedTextColor.RED, TextDecoration.BOLD))
                        .append(Component.text(")", NamedTextColor.WHITE, TextDecoration.BOLD))
                        .append(Component.text(" PVP is disabled in clan", NamedTextColor.YELLOW))
                        .append(Component.newline())
                        .append(Component.text("You can use /clan pvp on to enable pvp if you are the clan leader", NamedTextColor.GREEN));
                damager.sendMessage(msg);
                event.setCancelled(true);
            }
        }
    }

}
