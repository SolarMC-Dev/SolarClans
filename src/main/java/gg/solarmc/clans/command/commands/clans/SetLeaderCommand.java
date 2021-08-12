package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.ClanSetLeaderConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.SolarPlayer;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClanMember;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetLeaderCommand implements SubCommand {
    private final SolarClans plugin;

    public SetLeaderCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanSetLeaderConfig commandConfig = pluginConfig.clanSetLeader();

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;

        Component confirmMsg = helper.replaceText(pluginConfig.confirmMsg(),
                Map.of("{command}", "/clan setleader [Player Name] confirm",
                        "{action}", "transfer the Clan"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/clan setleader " + args[0] + " confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

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
        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            SolarPlayer playerTransferred = dataCenter.lookupPlayerUsing(transaction, args[0]).orElse(null);

            if (playerTransferred == null) {
                sender.sendMessage(pluginConfig.playerNotFound());
                return;
            }

            if (!clan.currentMembers().contains(new ClanMember(playerTransferred.getUserId()))) {
                sender.sendMessage(commandConfig.playerAbsent());
                return;
            }

            clan.setLeader(transaction, playerTransferred);
            helper.sendClanMsg(server, clan,
                    helper.replaceText(commandConfig.setLeader(),
                            Map.of("{player}", player.getName(),
                                    "{newPlayer}", playerTransferred.getMcUsername())));
        }).exceptionally(ex -> {
            player.sendMessage(pluginConfig.error());
            helper.getLogger().error("Something went wrong transferring a clan to another player", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "setleader";
    }

    @Override
    public String getArgs() {
        return "[Player Name]";
    }

    @Override
    public String getDescription() {
        return "Transfer the rank of leader to another member of the clan.";
    }
}
