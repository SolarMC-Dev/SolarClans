package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to Invite!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        // TODO: check if player is clan's leader

        // Send Player Kicked message to Clan Members
        Player playerKicked = player.getServer().getPlayer(args[0]);

        // TODO: look for Offline players too
        if (playerKicked == null) {
            player.sendMessage("The player is offline!");
            return;
        }

        playerKicked.sendMessage(player.getName() + " kicked you from " + clan.currentClanName() + " Clan");

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(
                transaction -> clan.removeClanMember(transaction, playerKicked.getSolarPlayer())
        ).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't kick the player! Something went wrong, Please try again later!!");
            helper.getLogger().error("Something went wrong kicking a player from a clan", ex);
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
