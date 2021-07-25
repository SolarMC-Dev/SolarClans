package gg.solarmc.clans.command;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import gg.solarmc.loader.clans.Clan;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.arim.omnibus.util.ThisClass;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CommandHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThisClass.get());
    private final Map<Integer, Cache<Player, Clan>> invites = new HashMap<>();
    private final Map<Integer, Boolean> pvp = new HashMap<>();

    public Logger getLogger() {
        return LOGGER;
    }

    public boolean invalidateCommandSender(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!!");
            return true;
        }

        return false;
    }

    public boolean invalidateArgs(CommandSender player, String[] args, String msg) {
        if (args.length == 0) {
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    public boolean invalidateConfirm(Player player, String[] args, Component msg, int confirmIndex) {
        if (args.length == confirmIndex + 1) {
            player.sendMessage(msg);
            return true;
        }

        if (!args[confirmIndex].equals("confirm")) {
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    public boolean hasInvited(Clan clan, Player player) {
        return invites.computeIfAbsent(clan.getID(), id -> getCache()
                .evictionListener((solarPlayer, solarClan, cause) -> {
                    String name = ((Clan) solarClan).getName();
                    ((Player) solarPlayer).sendMessage(ChatColor.YELLOW + "Clan invitation from " + name + " has expired");
                })
                .build())
                .getIfPresent(player) != null;
    }

    public void addInvite(Clan clan, Player solarPlayer) {
        Cache<Player, Clan> playersInvited = invites.computeIfAbsent(clan.getID(), id -> getCache().build());
        playersInvited.put(solarPlayer, clan);
        invites.put(clan.getID(), playersInvited);
    }

    private Caffeine<Object, Object> getCache() {
        return Caffeine.newBuilder().expireAfterAccess(Duration.ofSeconds(60));
    }

    public boolean isPvpOn(Clan clan) {
        return pvp.computeIfAbsent(clan.getID(), id -> true);
    }

    public void setPvp(Clan clan, boolean pvpOn) {
        pvp.put(clan.getID(), pvpOn);
    }

    public void sendNotInClanMsg(Player player) {
        String[] msg = {
                ChatColor.GOLD + "You are not in a Clan!",
                "Join a clan using /clan join [ClanName] if you are invited :)",
                "Create a clan using /clan create [ClanName]"
        };

        player.sendMessage(msg);
    }
}
