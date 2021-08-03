package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand implements SubCommand {

    private final SolarClans plugin;

    public RemoveCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(Component.text("Only Clan Leader can use this Command", NamedTextColor.RED));
            return;
        }

        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/ally remove confirm", NamedTextColor.GOLD))
                .append(Component.text(" to remove the Ally :)", NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("clan disband confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        Component errorMsg = plugin.getPluginConfig().error();

        player.getServer().getDataCenter().runTransact(transaction -> {
            if (!clan.revokeAlly(transaction)) {
                sender.sendMessage(ChatColor.RED + "You don't have a ally!!");
                return;
            }
            sender.sendMessage(plugin.getPluginConfig().allyRevoked());
        }).exceptionally(e -> {
            sender.sendMessage(errorMsg);
            helper.getLogger().error("Cannot revoke a Ally", e);
            return null;
        });
    }

    @Override
    public String getName() {
        return "allyremove";
    }

    @Override
    public String getArgs() {
        return "[confirm]";
    }

    @Override
    public String getDescription() {
        return "Removes a Ally from you Clan if any";
    }
}
