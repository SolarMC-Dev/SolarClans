package gg.solarmc.clans.events;

import gg.solarmc.clans.PVPHelper;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public record HitEvent(CommandHelper helper, PVPHelper clanPvp, PVPHelper allyPvp) implements Listener {

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || !(event.getDamager() instanceof Player damager))
            return;

        // TODO: return , If global pvp is disabled at that location

        Clan damagerClan = damager.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        Clan damagedPlayerClan = damagedPlayer.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (damagerClan == null || damagedPlayerClan == null) return;

        if (damagerClan.equals(damagedPlayerClan))
            if (!clanPvp.isPvpOn(damagerClan)) {
                damager.sendMessage(getPvPDisabledMsg("clan"));
                event.setCancelled(true);
            }

        Clan allyClan = damagerClan.currentAllyClan().orElse(null);
        if (allyClan == null) return;

        if(damagedPlayerClan.equals(allyClan))
            if(!allyPvp.isPvpOn(damagerClan) || !allyPvp().isPvpOn(damagedPlayerClan)){
                damager.sendMessage(getPvPDisabledMsg("ally"));
                event.setCancelled(true);
            }
    }

    private TextComponent getPvPDisabledMsg(String clanOrAlly) {
        return Component.text("(", NamedTextColor.WHITE, TextDecoration.BOLD)
                .append(Component.text("!", NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text(")", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(" PVP is disabled in " + clanOrAlly, NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(Component.text("You can use /" + clanOrAlly + " pvp on to enable pvp if you are the clan leader", NamedTextColor.GREEN));
    }

}
