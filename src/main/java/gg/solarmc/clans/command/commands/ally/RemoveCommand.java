package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.AllyRemoveConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

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

        MessageConfig pluginConfig = plugin.getPluginConfig();
        AllyRemoveConfig commandConfig = pluginConfig.allyRemove();

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(pluginConfig.leaderCommand());
            return;
        }

        Component confirmMsg = helper.replaceText(pluginConfig.confirmMsg(),
                Map.of("{command}", "/ally remove confirm",
                        "{action}", "remove the Ally"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/ally remove confirm")));
        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        Component errorMsg = pluginConfig.error();

        player.getServer().getDataCenter().runTransact(transaction -> {
            if (!clan.revokeAlly(transaction)) {
                sender.sendMessage(commandConfig.noAlly());
                return;
            }
            sender.sendMessage(commandConfig.revoked());
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
