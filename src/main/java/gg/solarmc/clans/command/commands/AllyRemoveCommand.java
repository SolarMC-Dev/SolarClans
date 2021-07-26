package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllyRemoveCommand implements ClansSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;
        Player player = (Player) sender;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        Component confirmMsg = Component.text("Confirm Message : Use ", NamedTextColor.YELLOW)
                .append(Component.text("/clan allyremove confirm", NamedTextColor.GOLD))
                .append(Component.text(" to remove the Ally :)"));

        if (helper.invalidateConfirm(player, args, confirmMsg, 0)) return;

        player.getServer().getDataCenter().runTransact(clan::revokeAlly);
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
