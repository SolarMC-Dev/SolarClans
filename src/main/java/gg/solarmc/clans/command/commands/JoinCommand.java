package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements ClansSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!!");
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You need to specify the Name of the Clan you want to Join!!");
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            int name = 0; // Make this args[0]
            Clan clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClan(transaction, name);

            if (!helper.hasInvited(clan, player)) {
                player.sendMessage("You are not invited to this clan!!");
                return;
            }

            // Send Join message to Clan Members
            // clan.getClanMembers(transaction).forEach();
            clan.addClanMember(transaction, player.getSolarPlayer());
        }).exceptionally((ex) -> {
            player.sendMessage(ChatColor.RED + "Something went wrong! Please try again Later");
            return null;
        });

    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getArgs() {
        return "[Clan Name]";
    }

    @Override
    public String getDescription() {
        return "Joins the clan with the name You invited";
    }
}
