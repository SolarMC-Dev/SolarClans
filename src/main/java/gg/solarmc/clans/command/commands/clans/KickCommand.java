package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
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
import org.slf4j.Logger;

public class KickCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to Invite!!")) return;

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
        DataCenter dataCenter = server.getDataCenter();

        Logger logger = helper.getLogger();

        SolarPlayer solarPlayerKicked = dataCenter.lookupPlayer(args[0])
                .thenApplyAsync(o -> o.orElse(null))
                .exceptionally(e -> {
                    logger.error("Cannot lookup player", e);
                    return null;
                }).getNow(null);

        if (solarPlayerKicked == null) {
            player.sendMessage(ChatColor.RED + "Cannot find the player!");
            return;
        }

        if (clan.currentMembers().contains(new ClanMember(solarPlayerKicked.getUserId()))) {
            player.sendMessage(ChatColor.RED + "Player is not in your clan!!");
            return;
        }

        Player playerKicked = server.getPlayer(solarPlayerKicked.getMcUuid());

        dataCenter.runTransact(transaction -> {
            helper.sendClanMsg(server, clan, Component.text(player.getName() + " kicked " + playerKicked.getName() + " from the clan", NamedTextColor.YELLOW));
            clan.removeClanMember(transaction, solarPlayerKicked);
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't kick the player! Something went wrong, Please try again later!!");
            logger.error("Something went wrong kicking a player from a clan", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getArgs() {
        return "[Player Name]";
    }

    @Override
    public String getDescription() {
        return "Kicks a player from the clan";
    }
}
