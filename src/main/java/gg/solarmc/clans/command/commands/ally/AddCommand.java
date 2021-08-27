package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.config.configs.ally.AllyAddConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class AddCommand implements SubCommand {

    private final SolarClans plugin;

    public AddCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        MessageConfig pluginConfig = plugin.getPluginConfig();
        AllyAddConfig commandConfig = pluginConfig.ally().add();

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(pluginConfig.leaderCommand());
            return;
        }

        if (clan.currentAllyClan().orElse(null) != null) {
            sender.sendMessage(commandConfig.allyPresent());
            return;
        }

        if (helper.isClanPresentInAlly(clan)) {
            sender.sendMessage(helper.replaceText(commandConfig.onGoingRequest(), "{clan}", helper.getAllyInvite(clan).currentClanName()));
            return;
        }

        Server server = sender.getServer();
        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            Clan allyClan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if (allyClan == null) {
                sender.sendMessage(pluginConfig.clanNotExist());
                return;
            }

            if (helper.hasAllyInvited(allyClan, clan)) {
                if (!clan.addClanAsAlly(transaction, allyClan)) {
                    sender.sendMessage(helper.replaceText(commandConfig.clanHasALly(), "{clan}", allyClan.currentClanName()));
                    return;
                }

                helper.sendClanMsg(server, clan,
                        helper.replaceText(commandConfig.allied(), "{clan}", allyClan.currentClanName()));
                helper.sendClanMsg(server, allyClan,
                        helper.replaceText(commandConfig.allied(), "{clan}", clan.currentClanName()));

                helper.removeAllyInvite(clan);
                helper.removeAllyInvite(allyClan);
                return;
            }

            helper.addAllyInvite(clan, allyClan);
            helper.getPlayerBy(server, allyClan.currentLeader().userId()).thenAccept(allyLeader -> {
                if (allyLeader == null) {
                    player.sendMessage(ChatColor.RED + "The Clan Leader is not online!");
                    return;
                }
                String clanName = clan.currentClanName();
                Component requestMsg = helper.replaceText(commandConfig.request(),
                        Map.of("{sender}", sender.getName(),
                                "{clan}", clanName))
                        .append(Component.text("Click to Ally", NamedTextColor.YELLOW)
                                .hoverEvent(HoverEvent.showText(Component.text("/ally add " + clanName)))
                                .clickEvent(ClickEvent.runCommand("/ally add " + clanName)));

                allyLeader.sendMessage(requestMsg);
            }).exceptionally(e -> {
                helper.getLogger().error("Couldn't find the player", e);
                return null;
            });
        }).exceptionally(e -> {
            helper.getLogger().error("Something went wrong adding a clan", e);
            return null;
        });
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getArgs() {
        return "[Ally Name]";
    }

    @Override
    public String getDescription() {
        return "Sends a request to a Clan or accepts the request from a Clan if there";
    }
}
