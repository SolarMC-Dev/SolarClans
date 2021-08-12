package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PVPCommand implements SubCommand {
    private final SolarClans plugin;
    private final PVPHelper pvpHelper;

    public PVPCommand(SolarClans plugin, PVPHelper pvpHelper) {
        this.plugin = plugin;
        this.pvpHelper = pvpHelper;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;

        Player player = (Player) sender;
        final Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }
        MessageConfig config = plugin.getPluginConfig();

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(config.leaderCommand());
            return;
        }
        Server server = player.getServer();

        if (args.length == 0) {
            pvpHelper.setPvp(clan, !pvpHelper.isPvpOn(clan));
            helper.sendClanMsg(server, clan, helper.replaceText(config.clanPVP(), "{mode}", getOnOrOff(pvpHelper.isPvpOn(clan))));
            return;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "on", "true", "enable" -> pvpHelper.setPvp(clan, true);
            case "off", "false", "disable" -> pvpHelper.setPvp(clan, false);
        }
        helper.sendClanMsg(server, clan, helper.replaceText(config.clanPVP(), "{mode}", getOnOrOff(pvpHelper.isPvpOn(clan))));
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
