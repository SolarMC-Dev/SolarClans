package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record CreateCommand(SolarClans plugin) implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan create [Clan Name] confirm", NamedTextColor.GOLD))
                .append(Component.text(" to create a Clan :)"));
        if (helper.invalidateCommandSender(sender, args)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan!!")) return;
        Player player = (Player) sender;
        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, 1000);

        if (!response.transactionSuccess()) {
            player.sendMessage(ChatColor.RED + "You don't have enough money to Create a Clan!!");
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

            // TODO: Check if clan exist

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
        return "[Clan Name] [confirm]";
    }

    @Override
    public String getDescription() {
        return "Creates a Clan - costs $1000";
    }
}
