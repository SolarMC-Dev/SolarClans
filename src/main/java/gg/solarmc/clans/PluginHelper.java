package gg.solarmc.clans;

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

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class PluginHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThisClass.get());
    private final Map<Integer, Cache<Player, Clan>> invites = new HashMap<>();
    private final Cache<Clan, Clan> allyInvites;

    public PluginHelper() {
        allyInvites = getCache()
                .evictionListener((clan, allyClan, cause) -> {
                    String name = ((Clan) clan).getName();
                    // TODO: sendMsg -> leader -> Clan invitation to solarPlayer has expired
                    // ((Clan)allyClan).currentLeader().sendMessage(ChatColor.YELLOW + "Ally invitation from " + name + " has expired");
                }).build();
    }

    public Logger getLogger() {
        return LOGGER;
    }

    // Validate Methods

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

    // Clan Invites to Player Methods

    public boolean hasInvited(Clan clan, Player player) {
        return invites.computeIfAbsent(clan.getID(), id -> getPlayerInviteCache())
                .getIfPresent(player) != null;
    }

    public void addInvite(Clan clan, Player player) {
        Cache<Player, Clan> playersInvited = invites.computeIfAbsent(clan.getID(), id -> getPlayerInviteCache());
        playersInvited.put(player, clan);
        invites.put(clan.getID(), playersInvited);
    }

    public void removeInvite(Clan clan, Player player) {
        Cache<Player, Clan> playersInvited = invites.computeIfAbsent(clan.getID(), id -> getPlayerInviteCache());
        playersInvited.invalidate(player);
        invites.put(clan.getID(), playersInvited);
    }

    // Clan Ally Invites Methods

    public boolean isClanPresentInAlly(Clan clan) {
        return allyInvites.getIfPresent(clan) != null;
    }

    public boolean hasAllyInvited(Clan clan, Clan allyClan) {
        return allyInvites.getIfPresent(clan).equals(allyClan);
    }

    public Clan getAllyInvite(Clan clan) {
        return allyInvites.getIfPresent(clan);
    }

    public void addAllyInvite(Clan clan, Clan allyClan) {
        allyInvites.put(clan, allyClan);
    }

    public void removeAllyInvite(Clan clan) {
        allyInvites.invalidate(clan);
    }

    // Cache build Methods

    private Cache<Player, Clan> getPlayerInviteCache() {
        return getCache().evictionListener((solarPlayer, solarClan, cause) -> {
            String name = ((Clan) solarClan).getName();
            // TODO: sendMsg -> leader -> Clan invitation to solarPlayer has expired
            ((Player) solarPlayer).sendMessage(ChatColor.YELLOW + "Clan invitation from " + name + " has expired");
        }).build();
    }

    private Caffeine<Object, Object> getCache() {
        return Caffeine.newBuilder().expireAfterAccess(Duration.ofSeconds(60));
    }

    // Msg methods o:

    public void sendNotInClanMsg(Player player) {
        String[] msg = {
                ChatColor.GOLD + "You are not in a Clan!",
                "Join a clan using /clan join [ClanName] if you are invited :)",
                "Create a clan using /clan create [ClanName]"
        };

        player.sendMessage(msg);
    }
}
