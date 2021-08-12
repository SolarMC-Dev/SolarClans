package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.OnlineSolarPlayer;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import gg.solarmc.loader.clans.OnlineClanDataObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class LeaveCommand implements SubCommand {
    private final SolarClans plugin;

    public LeaveCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();

        Component confirmMsg = helper.replaceText(pluginConfig.confirmMsg(),
                Map.of("{command}", "/clan leave confirm",
                        "{action}", "leave the Clan"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/clan leave confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        OnlineSolarPlayer solarPlayer = player.getSolarPlayer();
        OnlineClanDataObject clanMember = solarPlayer.getData(ClansKey.INSTANCE);
        Clan clan = clanMember.currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (clan.currentLeader().isSimilar(clanMember)) {
            player.performCommand("/clan disband confirm");
            return;
        }

        Server server = player.getServer();
        server.getDataCenter().runTransact(transaction -> {
            helper.sendClanMsg(server, clan, helper.replaceText(pluginConfig.clanLeave(), "{player}", player.getName()));
            clan.removeClanMember(transaction, solarPlayer);
        }).exceptionally(e -> {
            player.sendMessage(pluginConfig.error());
            helper.getLogger().error("Something went wrong removing a player from a Clan", e);
            return null;
        });
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getArgs() {
        return "[Player Name] confirm";
    }

    @Override
    public String getDescription() {
        return "Leaves the current Clan";
    }
}
