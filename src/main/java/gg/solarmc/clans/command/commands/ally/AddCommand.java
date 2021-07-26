package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.command.CommandHelper;
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
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You have to specify the name of the Clan you have to ally!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        // TODO: check if sender is Clan Leader;

        if (clan.currentAllyClan().orElse(null) != null) {
            String[] msg = {
                    ChatColor.RED + "You already have a ally clan",
                    ChatColor.YELLOW + "You can use /clan [allyremove|allyr] to remove your current ally"
            };
            sender.sendMessage(msg);
            return;
        }

        if (!helper.isClanPresentInAlly(clan)) {
            sender.sendMessage(ChatColor.RED + "You already have ongoing request to " + helper.getAllyInvite(clan).getName());
            return;
        }

        DataCenter dataCenter = sender.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            int name = 0; // Make this args[0]
            Clan allyClan = dataCenter.getDataManager(ClansKey.INSTANCE).getClan(transaction, name);

            if (allyClan.currentAllyClan().orElse(null) != null) {
                sender.sendMessage(ChatColor.RED + "The Clan " + allyClan.getName() + " already have a ally clan");
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

            TextComponent requestMsg = Component.text(sender.getName() + " has requested a Clan ally to " + clan.getName(), NamedTextColor.GREEN)
                    .append(Component.text("Click to Ally")
                            .clickEvent(ClickEvent.runCommand("clan ally " + clan.getName())));
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
