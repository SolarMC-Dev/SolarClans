package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.OnlineSolarPlayer;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
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
        Player player = (Player) sender;

        OnlineSolarPlayer solarPlayer = player.getSolarPlayer();
        if (solarPlayer.getData(ClansKey.INSTANCE).currentClan().isPresent()) {
            player.sendMessage(ChatColor.RED + "You are already in a Clan!!");
            return;
        }

        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan!!")) return;

        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan create [Clan Name] confirm", NamedTextColor.GOLD))
                .append(Component.text(" to create a Clan :)"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/clan create " + args[0] + " confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

        /*EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, 1000);

        if (!response.transactionSuccess()) {
            player.sendMessage(ChatColor.RED + "You don't have enough money to Create a Clan!!");
            return;
        }*/

        MessageConfig config = plugin.getPluginConfig();

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

            if (manager.getClanByName(transaction, args[0]).orElse(null) != null) {
                sender.sendMessage(ChatColor.RED + "Clan already exist!!");
                return;
            }

            manager.createClan(transaction, args[0], solarPlayer)
                    .addClanMember(transaction, solarPlayer);
            player.sendMessage(helper.replaceText(config.clanCreated(), "{clan}", args[0]));
        }).exceptionally((ex) -> {
            player.sendMessage(config.error());
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
