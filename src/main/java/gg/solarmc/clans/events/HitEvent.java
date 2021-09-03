package gg.solarmc.clans.events;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import subside.plugins.koth.gamemodes.RunningKoth;

public record HitEvent(SolarClans plugin) implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        Player damager;
        if (event.getEntity() instanceof Player damagedPlayer) {
            Entity player = event.getDamager();
            if (player instanceof Player) {
                damager = (Player) player;
            } else if (player instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) player).getShooter();
                if (shooter instanceof Player) {
                    damager = (Player) shooter;
                } else
                    return;
            } else
                return;
        } else
            return;

        if (damager.getUniqueId().equals(damagedPlayer.getUniqueId())) return;

        WorldGuardPlugin wgManager = plugin.getWorldGuardManager();
        ApplicableRegionSet damagerRegions = wgManager.getRegionManager(damager.getWorld()).getApplicableRegions(damager.getLocation());
        ApplicableRegionSet damagedRegions = wgManager.getRegionManager(damagedPlayer.getWorld()).getApplicableRegions(damagedPlayer.getLocation());

        if (damagerRegions.queryState(r -> Association.NON_MEMBER, DefaultFlag.PVP) == StateFlag.State.DENY
                || damagedRegions.queryState(r -> Association.NON_MEMBER, DefaultFlag.PVP) == StateFlag.State.DENY)
            return;

        PVPHelper clanPvp = plugin.getClanPvpHelper();
        PVPHelper allyPvp = plugin.getAllyPvpHelper();

        Clan damagerClan = damager.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        Clan damagedPlayerClan = damagedPlayer.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (damagerClan == null || damagedPlayerClan == null) return;

        if (damagerClan.equals(damagedPlayerClan)) {
            if (!clanPvp.isPvpOn(damagerClan)) {
                damager.sendMessage(getPvPDisabledMsg("clan"));
                event.setCancelled(true);
            }
        }

        RunningKoth rKoth = plugin.getKoth().getKothHandler().getRunningKoth();
        if (rKoth != null) return;

        Clan allyClan = damagerClan.currentAllyClan().orElse(null);
        if (allyClan == null) return;

        if (damagedPlayerClan.equals(allyClan)) {
            if (!allyPvp.isPvpOn(damagerClan) || !allyPvp.isPvpOn(damagedPlayerClan)) {
                damager.sendMessage(getPvPDisabledMsg("ally"));
                event.setCancelled(true);
            }
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
