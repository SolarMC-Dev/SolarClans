package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.SolarPlayer;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClanMember;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLeaderCommand implements SubCommand {
    private final SolarClans plugin;

    public SetLeaderCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to transfer this Clan!!")) return;

        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan setleader [Player Name] confirm", NamedTextColor.GOLD))
                .append(Component.text(" to create a Clan :)"));

        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        MessageConfig config = plugin.getPluginConfig();

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(config.leaderCommand());
            return;
        }

        Server server = player.getServer();
        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            SolarPlayer playerTransferred = dataCenter.lookupPlayerUsing(transaction, args[0]).orElse(null);

            if (playerTransferred == null) {
                sender.sendMessage(ChatColor.RED + "Cannot find player!!");
                return;
            }

            if (!clan.currentMembers().contains(new ClanMember(playerTransferred.getUserId()))) {
                sender.sendMessage(ChatColor.RED + "The player should be in your Clan!!");
                return;
            }

            clan.setLeader(transaction, playerTransferred);
            helper.sendClanMsg(server, clan,
                    Component.text(player.getName() + " transferred the clan to ", NamedTextColor.GREEN)
                            .append(Component.text(args[0], NamedTextColor.GOLD)));
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't transfer the clan! Something went wrong, Please try again later!!");
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
