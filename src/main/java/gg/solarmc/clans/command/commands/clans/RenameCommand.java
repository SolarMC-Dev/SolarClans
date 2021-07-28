package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.PluginHelper;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        // TODO: check if player is clan's leader

        // Send Clan renamed message to Clan Members

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            // :)
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't rename the clan! Something went wrong, Please try again later!!");
            helper.getLogger().error("Something went wrong renaming a clan", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getArgs() {
        return "[Name]";
    }

    @Override
    public String getDescription() {
        return "Renames the clan";
    }
}
