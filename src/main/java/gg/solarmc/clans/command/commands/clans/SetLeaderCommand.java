package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLeaderCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to transfer this Clan!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        // TODO: check if player is clan's leader

        // Send Clan transferred message to Clan Members

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            // :)
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
