package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.ClanCreateConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.OnlineSolarPlayer;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CreateCommand implements SubCommand {
    private final SolarClans plugin;

    public CreateCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanCreateConfig commandConfig = pluginConfig.clanCreate();

        OnlineSolarPlayer solarPlayer = player.getSolarPlayer();
        if (solarPlayer.getData(ClansKey.INSTANCE).currentClan().isPresent()) {
            player.sendMessage(commandConfig.clanPresent());
            return;
        }

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;

        Component confirmMsg = helper.replaceText(pluginConfig.confirmMsg(),
                Map.of("{command}", "/clan create [Clan Name] confirm",
                        "{action}", "create a Clan"))
                .append(Component.newline())
                .append(Component.text("Click to Confirm")
                        .clickEvent(ClickEvent.runCommand("/clan create " + args[0] + " confirm")));

        if (helper.invalidateConfirm(player, args, confirmMsg, 1)) return;

        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, 1000);

        if (!response.transactionSuccess()) {
            player.sendMessage(commandConfig.notEnoughMoney());
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

            if (manager.getClanByName(transaction, args[0]).orElse(null) != null) {
                sender.sendMessage(commandConfig.clanNamePresent());
                return;
            }

            manager.createClan(transaction, args[0], solarPlayer)
                    .addClanMember(transaction, solarPlayer);
            player.sendMessage(helper.replaceText(commandConfig.created(), "{clan}", args[0]));
        }).exceptionally((ex) -> {
            player.sendMessage(pluginConfig.error());
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
