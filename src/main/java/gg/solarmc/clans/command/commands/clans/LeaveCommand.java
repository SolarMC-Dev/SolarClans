package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.OnlineSolarPlayer;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements SubCommand {
    private final SolarClans plugin;

    public LeaveCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        OnlineSolarPlayer solarPlayer = player.getSolarPlayer();
        Clan clan = solarPlayer.getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        Server server = player.getServer();
        server.getDataCenter().runTransact(transaction -> {
            helper.sendClanMsg(server, clan, Component.text(player.getName() + " left the Clan", NamedTextColor.YELLOW));
            clan.removeClanMember(transaction, solarPlayer);
        }).exceptionally(e -> {
            player.sendMessage(plugin.getPluginConfig().error());
            helper.getLogger().error("Something went wrong removing a player from a Clan", e);
            return null;
        });
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getArgs() {
        return "[Player Name] confirm";
    }

    @Override
    public String getDescription() {
        return "Leaves the current Clan";
    }
}
