package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements ClansSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Player you want to Invite!!")) return;
        Player player = (Player) sender;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        DataCenter dataCenter = player.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            if (clan.getClanSize(transaction) == 5) {
                sender.sendMessage(ChatColor.RED + "There are already 5 players in the Clan!! You cannot invite more people! You can kick players by /clan kick command");
                return;
            }

            // TODO: check if player is clan's leader

            // Send Player Invited message to Clan Members
            Player playerInvited = player.getServer().getPlayer(args[0]);

            if (playerInvited == null) {
                player.sendMessage("The player is offline!");
                return;
            }

            TextComponent joinMsg = Component.text("Click to join Clan", NamedTextColor.YELLOW, TextDecoration.ITALIC).
                    clickEvent(ClickEvent.runCommand("clan join " + clan.getName()));
            TextComponent inviteMsg = Component.text(player.getName() + " invited you to " + clan.getName() + " Clan", NamedTextColor.GREEN)
                    .append(Component.newline())
                    .append(joinMsg);

            player.sendMessage(ChatColor.GREEN + "The player was successfully invited! :D");
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
