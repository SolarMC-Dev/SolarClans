package gg.solarmc.clans.command.commands.clans;

import com.drtshock.playervaults.vaultmanagement.VaultManager;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class DisbandCommand implements SubCommand {

    private final SolarClans plugin;

    public DisbandCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();

        Component confirmMsg = helper.replaceText(pluginConfig.confirmMsg(),
                Map.of("{command}", "/clan disband confirm",
                        "{action}", "disband the Clan"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/clan disband confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(pluginConfig.leaderCommand());
            return;
        }

        Server server = player.getServer();
        helper.sendClanMsg(server, clan,
                helper.replaceText(pluginConfig.clan().disband(), "{player}", player.getName()));

        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            helper.sendClanMsg(server, clan, pluginConfig.clan().disband());
            dataCenter.getDataManager(ClansKey.INSTANCE).deleteClan(transaction, clan);
        })
                .thenRunSync(() -> VaultManager.getInstance().deleteAllVaults(("clan_vault:" + clan.getClanId())))
                .exceptionally(ex -> {
                    player.sendMessage(pluginConfig.error());
                    helper.getLogger().error("Something went wrong disbanding a clan", ex);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public String getArgs() {
        return "[confirm]";
    }

    @Override
    public String getDescription() {
        return "Disbands your clan";
    }
}
