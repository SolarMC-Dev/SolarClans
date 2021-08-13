package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.clan.ClanInviteConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClanMember;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class InviteCommand implements SubCommand {
    private final SolarClans plugin;

    public InviteCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanInviteConfig commandConfig = pluginConfig.clan().invite();

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;
        Player player = (Player) sender;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(pluginConfig.leaderCommand());
            return;
        }

        Server server = player.getServer();
        Player playerInvited = server.getPlayer(args[0]);
        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            if (clan.getClanSize(transaction) == 5) {
                sender.sendMessage(commandConfig.maxPlayersReached());
                return;
            }

            if (playerInvited != null && clan.currentMembers().contains(new ClanMember(playerInvited.getSolarPlayer().getUserId()))) {
                sender.sendMessage(commandConfig.playerPresent());
                return;
            }

            if (playerInvited == null) {
                player.sendMessage(pluginConfig.playerOffline());
                return;
            }

            if (playerInvited.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().isPresent()) {
                sender.sendMessage(commandConfig.playerPresentInClan());
                return;
            }

            helper.sendClanMsg(server, clan,
                    helper.replaceText(commandConfig.playerInvited(),
                            Map.of("{player}", player.getName(),
                                    "{playerInvited}", playerInvited.getName())));

            TextComponent inviteMsg = Component.text(player.getName() + " invited you to " + clan.currentClanName() + " Clan", NamedTextColor.GREEN)
                    .append(Component.newline())
                    .append(Component.text("Click to join Clan", NamedTextColor.YELLOW, TextDecoration.ITALIC).
                            clickEvent(ClickEvent.runCommand("/clan join " + clan.currentClanName())));

            playerInvited.sendMessage(inviteMsg);
            helper.addInvite(clan, playerInvited);
        }).exceptionally(ex -> {
            player.sendMessage(pluginConfig.error());
            helper.getLogger().error("Couldn't get the size of a Clan", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getArgs() {
        return "[PlayerName]";
    }

    @Override
    public String getDescription() {
        return "Invites a player to your Clan";
    }
}
