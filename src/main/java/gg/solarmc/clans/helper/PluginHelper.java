package gg.solarmc.clans.helper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.loader.clans.Clan;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.arim.omnibus.util.ThisClass;
import space.arim.omnibus.util.concurrent.CentralisedFuture;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class PluginHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThisClass.get());
    private final SolarClans plugin;
    private final Map<Integer, Cache<Player, Clan>> invites = new HashMap<>();
    private final Cache<Clan, Clan> allyInvites;

    public PluginHelper(SolarClans plugin) {
        this.plugin = plugin;
        allyInvites = buildCache()
                .evictionListener((clan, allyClan, cause) -> {
                    final Clan solarClan = (Clan) clan;
                    getPlayerBy(plugin.getServer(), ((Clan) allyClan).currentLeader().userId()).thenAccept(leader -> {
                        if (leader == null) return;
                        leader.sendMessage(ChatColor.YELLOW + "Ally invitation from " + solarClan.currentClanName() + " has expired");
                    });
                }).build();
    }

    public Logger getLogger() {
        return LOGGER;
    }

    // Validate Methods

    public boolean invalidateCommandSender(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!!");
            return true;
        }

        return false;
    }

    public boolean invalidateArgs(CommandSender player, String[] args, Component msg) {
        if (args.length == 0) {
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    public boolean invalidateConfirm(Player player, String[] args, Component msg, int confirmIndex) {
        if (args.length != confirmIndex + 1) {
            player.sendMessage(msg);
            return true;
        }

        if (!args[confirmIndex].equals("confirm")) {
            player.sendMessage(msg);
            return true;
        }
        return false;
    }

    // Clan Methods

    public boolean isLeader(Clan clan, Player player) {
        return player.getSolarPlayer().getUserId() == clan.currentLeader().userId();
    }

    // Clan Invites to Player Methods

    public boolean hasInvited(Clan clan, Player player) {
        return invites.computeIfAbsent(clan.getClanId(), id -> getPlayerInviteCache())
                .getIfPresent(player) != null;
    }

    public void addInvite(Clan clan, Player player) {
        Cache<Player, Clan> playersInvited = invites.computeIfAbsent(clan.getClanId(), id -> getPlayerInviteCache());
        playersInvited.put(player, clan);
        invites.put(clan.getClanId(), playersInvited);
    }

    public void removeInvite(Clan clan, Player player) {
        Cache<Player, Clan> playersInvited = invites.computeIfAbsent(clan.getClanId(), id -> getPlayerInviteCache());
        playersInvited.invalidate(player);
        invites.put(clan.getClanId(), playersInvited);
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
        return buildCache().evictionListener((solarPlayer, solarClan, cause) -> {
            Clan clan = (Clan) solarClan;
            Player player = (Player) solarPlayer;

            player.sendMessage(ChatColor.YELLOW + "Clan invitation from " + clan.currentClanName() + " has expired");
            getPlayerBy(player.getServer(), clan.currentLeader().userId()).thenAccept(leader -> {
                if (leader == null) return;
                leader.sendMessage(ChatColor.YELLOW + "Invitation to " + player.getName() + " has expired");
            });
        }).build();
    }

    private Caffeine<Object, Object> buildCache() {
        return Caffeine.newBuilder().expireAfterAccess(Duration.ofSeconds(60));
    }

    // Msg methods o:

    public void sendNotInClanMsg(Player player) {
        player.sendMessage(plugin.getPluginConfig().notInClan());
    }

    public void sendClanMsg(Server server, Clan clan, Component msg) {
        clan.currentMembers().forEach(it -> {
            getPlayerBy(server, it.userId()).thenAccept(player -> {
                if (player == null) return;
                System.out.println(player.isOnline());

                if (player.isOnline())
                    player.sendMessage(msg);
            });
        });
    }

    public Component replaceText(Component component, String target, String replacement) {
        return component.replaceText(builder -> builder.matchLiteral(target).replacement(replacement));
    }

    public Component replaceText(Component component, Map<String, String> replacements) {
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            component = component.replaceText(builder ->
                    builder.matchLiteral(replacement.getKey())
                            .replacement(replacement.getValue()));
        }

        return component;
    }

    public CentralisedFuture<Player> getPlayerBy(Server server, int id) {
        return server.getDataCenter().lookupPlayer(id)
                .thenApplySync(o -> o.orElse(null))
                .thenApplySync(sPlayer -> {
                    if (sPlayer == null) return null;
                    return server.getPlayer(sPlayer.getMcUuid());
                })
                .exceptionally(e -> {
                    getLogger().error("Cannot lookup player", e);
                    return null;
                });
    }

}
