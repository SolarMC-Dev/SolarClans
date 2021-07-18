package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record CreateCommand(SolarClans plugin) implements ClansSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!!");
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You need to specify the Name of the Clan!!");
            return;
        }

        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, 1000);

        if (!response.transactionSuccess()) {
            player.sendMessage(ChatColor.RED + "You don't have enough money to Create a Clan!!");
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

            // TODO: Check if clan exist
            // TODO: Confirmation Msg

            manager.createClan(transaction, args[0], player.getSolarPlayer());
        })
                .thenRunAsync(() -> player.sendMessage(ChatColor.GREEN + "Clan Created : " + args[0]))
                .exceptionally((ex) -> {
                    player.sendMessage(ChatColor.DARK_RED + "Something went wrong, Please try again Later!");
                    helper.getLogger().error("Couldn't make a clan, command used by " + player.getName());
                    return null;
                });
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getArgs() {
        return "[Clan Name]";
    }

    @Override
    public String getDescription() {
        return "Creates a Clan - costs $1000";
    }
}
