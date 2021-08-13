package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.clan.ClanKickConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClanMember;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.Map;

public class KickCommand implements SubCommand {
    private final SolarClans plugin;

    public KickCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanKickConfig commandConfig = pluginConfig.clan().kick();


        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(pluginConfig.leaderCommand());
            return;
        }

        Server server = player.getServer();
        DataCenter dataCenter = server.getDataCenter();

        Logger logger = helper.getLogger();

        dataCenter.lookupPlayer(args[0])
                .thenApply(o -> o.orElse(null))
                .thenApplySync(solarPlayerKicked -> {
                    if (solarPlayerKicked == null) {
                        player.sendMessage(pluginConfig.playerNotFound());
                        return null;
                    }

                    if (clan.currentMembers().contains(new ClanMember(solarPlayerKicked.getUserId()))) {
                        player.sendMessage(commandConfig.playerAbsent());
                        return null;
                    }

                    dataCenter.runTransact(transaction -> {
                        helper.sendClanMsg(server, clan,
                                helper.replaceText(commandConfig.kicked(),
                                        Map.of("{player}", player.getSolarPlayer().getMcUsername(),
                                                "{playerKicked}", solarPlayerKicked.getMcUsername())));
                        clan.removeClanMember(transaction, solarPlayerKicked);
                    }).exceptionally(ex -> {
                        player.sendMessage(pluginConfig.error());
                        logger.error("Something went wrong kicking a player from a clan", ex);
                        return null;
                    });

                    return null;
                })
                .exceptionally(e -> {
                    logger.error("Cannot lookup player", e);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getArgs() {
        return "[Player Name]";
    }

    @Override
    public String getDescription() {
        return "Kicks a player from the clan";
    }
}
