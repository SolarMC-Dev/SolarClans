package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;

        // TODO : Check if the sender is clan leader.
        Player player = (Player) sender;
        final Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (args.length == 0) {
            helper.setPvp(clan, !helper.isPvpOn(clan));
            player.sendMessage(Component.text("PVP has been toggle to ", NamedTextColor.GREEN)
                    .append(Component.text(getOnOrOff(helper.isPvpOn(clan)), NamedTextColor.GOLD)));
            return;
        }

        args[0] = args[0].toLowerCase();

        if (args[0].equals("on") || args[0].equals("true")) helper.setPvp(clan, true);
        else if (args[0].equals("off") || args[0].equals("false")) helper.setPvp(clan, false);
        else {
            player.sendMessage(Component.text("You need to specify on/off!!", NamedTextColor.RED));
            return;
        }

        player.sendMessage(Component.text("PVP has been set to ", NamedTextColor.GREEN)
                .append(Component.text(getOnOrOff(helper.isPvpOn(clan)), NamedTextColor.GOLD)));
    }

    public String getOnOrOff(boolean b) {
        return b ? "on" : "off";
    }

    @Override
    public String getName() {
        return "pvp";
    }

    @Override
    public String getArgs() {
        return "[on|off/Optional]";
    }

    @Override
    public String getDescription() {
        return "Turns the pvp on or off within the clan";
    }
}
