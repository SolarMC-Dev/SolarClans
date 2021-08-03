package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AddCommand implements SubCommand {

    private final SolarClans plugin;

    public AddCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You have to specify the name of the Clan you have to ally!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(Component.text("Only Clan Leader can use this Command", NamedTextColor.RED));
            return;
        }

        if (clan.currentAllyClan().orElse(null) != null) {
            String[] msg = {
                    ChatColor.RED + "You already have a ally clan",
                    ChatColor.YELLOW + "You can use /ally remove to remove your current ally"
            };
            sender.sendMessage(msg);
            return;
        }

        if (!helper.isClanPresentInAlly(clan)) {
            sender.sendMessage(ChatColor.RED + "You already have ongoing request to " + helper.getAllyInvite(clan).currentClanName());
            return;
        }

        Server server = sender.getServer();
        DataCenter dataCenter = server.getDataCenter();

        AtomicBoolean sendRequest = new AtomicBoolean(false);
        AtomicInteger allyLeaderId = new AtomicInteger();

        dataCenter.runTransact(transaction -> {
            Clan allyClan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if (allyClan == null) {
                sender.sendMessage(plugin.getPluginConfig().clanNotExist());
                return;
            }

            if (allyClan.currentAllyClan().orElse(null) != null) {
                sender.sendMessage(ChatColor.RED + "The Clan " + allyClan.currentClanName() + " already has an ally clan");
                return;
            }

            if (helper.hasAllyInvited(allyClan, clan)) {
                clan.addClanAsAlly(transaction, allyClan);
                Component alliedMsg = helper.replaceText(plugin.getPluginConfig().allyAllied(), "{clan}", allyClan.getClanName(transaction));

                helper.sendClanMsg(server, clan, alliedMsg.append(Component.text(allyClan.getClanName(transaction), NamedTextColor.GOLD)));
                helper.sendClanMsg(server, allyClan, alliedMsg.append(Component.text(clan.getClanName(transaction), NamedTextColor.GOLD)));

                helper.removeAllyInvite(clan);
                helper.removeAllyInvite(allyClan);
                return;
            }

            helper.addAllyInvite(clan, allyClan);
            sendRequest.set(true);
            allyLeaderId.set(allyClan.currentLeader().userId());
        });

        if (allyLeaderId.get() == 0) return;

        if (sendRequest.get()) {
            String clanName = clan.currentClanName();
            TextComponent requestMsg = Component.text(sender.getName() + " has requested a Clan Ally to " + clanName, NamedTextColor.GREEN)
                    .append(Component.text("Click to Ally")
                            .clickEvent(ClickEvent.runCommand("clan ally " + clanName)));
            Player allyLeader = helper.getPlayerBy(server, allyLeaderId.get());
            if (allyLeader == null) {
                player.sendMessage(ChatColor.RED + "The Clan Leader is not online!");
                return;
            }
            allyLeader.sendMessage(requestMsg);
        }
    }

    @Override
    public String getName() {
        return "ally";
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
