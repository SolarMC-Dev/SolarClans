package gg.solarmc.clans.command.commands.ally;

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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You have to specify the name of the Clan you have to ally!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if(!helper.isLeader(clan, player)){
            player.sendMessage(Component.text("Only Clan Leader can use this Command", NamedTextColor.RED));
            return;
        }

        if (clan.currentAllyClan().orElse(null) != null) {
            String[] msg = {
                    ChatColor.RED + "You already have a ally clan",
                    ChatColor.YELLOW + "You can use /clan [allyremove|allyr] to remove your current ally"
            };
            sender.sendMessage(msg);
            return;
        }

        if (!helper.isClanPresentInAlly(clan)) {
            sender.sendMessage(ChatColor.RED + "You already have ongoing request to " + helper.getAllyInvite(clan).currentClanName());
            return;
        }

        DataCenter dataCenter = sender.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            Clan allyClan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if (allyClan == null) {
                sender.sendMessage(ChatColor.RED + "Clan does not exist!");
                return;
            }

            if (allyClan.currentAllyClan().orElse(null) != null) {
                sender.sendMessage(ChatColor.RED + "The Clan " + allyClan.currentClanName()+ " already have a ally clan");
                return;
            }

            if (helper.hasAllyInvited(allyClan, clan)) {
                clan.addClanAsAlly(transaction, allyClan);
                final TextComponent alliedMsg = Component.text("You are been allied to ", NamedTextColor.GREEN);
                // Send alliedMsg to clan members
                // Send alliedMsg to AllyClan members
                helper.removeAllyInvite(clan);
                helper.removeAllyInvite(allyClan);
                return;
            }

            helper.addAllyInvite(clan, allyClan);

             String clanName = clan.currentClanName();
            TextComponent requestMsg = Component.text(sender.getName() + " has requested a Clan ally to " + clanName, NamedTextColor.GREEN)
                    .append(Component.text("Click to Ally")
                            .clickEvent(ClickEvent.runCommand("clan ally " + clanName)));
            // Send requestMsg to allyClan.currentLeader();
        });
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
