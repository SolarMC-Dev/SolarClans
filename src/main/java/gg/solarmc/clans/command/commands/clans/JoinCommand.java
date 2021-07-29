package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan you want to Join!!")) return;
        Player player = (Player) sender;

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            Clan clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if(clan == null){
                sender.sendMessage("Clan does not exist!");
                return;
            }

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
