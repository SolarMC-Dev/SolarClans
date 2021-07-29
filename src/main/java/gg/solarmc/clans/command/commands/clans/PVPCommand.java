package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PVPCommand implements SubCommand {
    private final PVPHelper pvpHelper;

    public PVPCommand(PVPHelper pvpHelper) {
        this.pvpHelper = pvpHelper;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;

        Player player = (Player) sender;
        final Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(Component.text("Only Clan Leader can use this Command", NamedTextColor.RED));
            return;
        }

        if (args.length == 0) {
            pvpHelper.setPvp(clan, !pvpHelper.isPvpOn(clan));
            player.sendMessage(Component.text("PVP has been toggle to ", NamedTextColor.GREEN)
                    .append(Component.text(getOnOrOff(pvpHelper.isPvpOn(clan)), NamedTextColor.GOLD)));
            return;
        }

        args[0] = args[0].toLowerCase();

        if (args[0].equals("on") || args[0].equals("true")) pvpHelper.setPvp(clan, true);
        else if (args[0].equals("off") || args[0].equals("false")) pvpHelper.setPvp(clan, false);
        else {
            player.sendMessage(Component.text("You need to specify on/off!!", NamedTextColor.RED));
            return;
        }

        helper.sendClanMsg(player.getServer(), clan,
                Component.text(player.getName() + " set the PVP ", NamedTextColor.GREEN)
                        .append(Component.text(getOnOrOff(pvpHelper.isPvpOn(clan)), NamedTextColor.GOLD)));
    }

    private String getOnOrOff(boolean b) {
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
