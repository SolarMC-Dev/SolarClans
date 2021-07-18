package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements ClansSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!!");
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You need to specify the Name of the Player you want to Invite!!");
            return;
        }

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            if (clan.getClanSize(transaction) == 5) {

                return;
            }

            // TODO: check if player is clan's leader

            // Send Player Invited message to Clan Members
            Player playerInvited = player.getServer().getPlayer(args[0]);

            if (playerInvited == null) {
                player.sendMessage("The player is offline!");
                return;
            }

            playerInvited.sendMessage(player.getName() + " invited you to " + clan.getName() + " Clan");
            // Click To Join
            helper.addInvite(clan, playerInvited);
        }).exceptionally(ex -> {
            player.sendMessage("Something went wrong, Please try again Later!");
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
