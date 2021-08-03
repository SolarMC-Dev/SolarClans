package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements SubCommand {
    private final SolarClans plugin;

    public CreateCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan!!")) return;
        Player player = (Player) sender;

        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan create [Clan Name] confirm", NamedTextColor.GOLD))
                .append(Component.text(" to create a Clan :)"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("clan create " + args[0] + " confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, 1000);

        if (!response.transactionSuccess()) {
            player.sendMessage(ChatColor.RED + "You don't have enough money to Create a Clan!!");
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

            if (manager.getClanByName(transaction, args[0]).orElse(null) != null) {
                sender.sendMessage(ChatColor.RED + "Clan already exist!!");
                return;
            }

            manager.createClan(transaction, args[0], player.getSolarPlayer());
            player.sendMessage(helper.replaceText(plugin.getPluginConfig().clanCreated(), "{clan}", args[0]));
        }).exceptionally((ex) -> {
            player.sendMessage(ChatColor.DARK_RED + "Something went wrong, Please try again Later!");
            helper.getLogger().error("Couldn't make a clan, command used by " + player.getName(), ex);
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
