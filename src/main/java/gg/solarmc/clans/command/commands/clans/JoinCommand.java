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

public class JoinCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan you want to Join!!")) return;
        Player player = (Player) sender;

        Server server = player.getServer();

        DataCenter dataCenter = server.getDataCenter();
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

            clan.addClanMember(transaction, player.getSolarPlayer());

            clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);
            helper.sendClanMsg(server, clan, Component.text(player.getName() + " joined the clan", NamedTextColor.YELLOW));
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
