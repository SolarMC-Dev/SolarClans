package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan disband confirm", NamedTextColor.GOLD))
                .append(Component.text(" to disband the Clan :)"));
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(Component.text("Only Clan Leader can use this Command", NamedTextColor.RED));
            return;
        }

        Server server = player.getServer();
        helper.sendClanMsg(server, clan, Component.text(player.getName() + " disbanded the clan", NamedTextColor.YELLOW));

        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> dataCenter.getDataManager(ClansKey.INSTANCE).deleteClan(transaction, clan)).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't disband the clan! Something went wrong, Please try again later!!");
            helper.getLogger().error("Something went wrong disbanding a clan", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public String getArgs() {
        return "[confirm]";
    }

    @Override
    public String getDescription() {
        return "Disbands your clan";
    }
}
