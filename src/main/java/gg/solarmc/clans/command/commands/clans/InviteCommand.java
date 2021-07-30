package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClanMember;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to Invite!!")) return;
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

        Server server = player.getServer();
        Player playerInvited = server.getPlayer(args[0]);
        DataCenter dataCenter = server.getDataCenter();

        dataCenter.runTransact(transaction -> {
            if (clan.getClanSize(transaction) == 5) {
                sender.sendMessage(ChatColor.RED + "There are already 5 players in the Clan!! You cannot invite more people! You can kick players by /clan kick command");
                return;
            }

            if (playerInvited != null && clan.currentMembers().contains(new ClanMember(playerInvited.getSolarPlayer().getUserId()))) {
                sender.sendMessage(ChatColor.YELLOW + "Player is already in your Cla");
                return;
            }

            if (playerInvited == null) {
                player.sendMessage("The player is offline!");
                return;
            }

            helper.sendClanMsg(server, clan, Component.text(player.getName() + " invited " + playerInvited.getName() + " to the clan", NamedTextColor.YELLOW));

            TextComponent inviteMsg = Component.text(player.getName() + " invited you to " + clan.currentClanName() + " Clan", NamedTextColor.GREEN)
                    .append(Component.newline())
                    .append(Component.text("Click to join Clan", NamedTextColor.YELLOW, TextDecoration.ITALIC).
                            clickEvent(ClickEvent.runCommand("clan join " + clan.currentClanName())));

            playerInvited.sendMessage(inviteMsg);
            helper.addInvite(clan, playerInvited);
        }).exceptionally(ex -> {
            player.sendMessage("Something went wrong, Please try again Later!");
            return null;
        });
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getArgs() {
        return "[PlayerName]";
    }

    @Override
    public String getDescription() {
        return "Invites a player to your Clan";
    }
}
