package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.clan.ClanRenameConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class RenameCommand implements SubCommand {
    private final SolarClans plugin;

    public RenameCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanRenameConfig commandConfig = pluginConfig.clan().rename();

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

        final Server server = player.getServer();

        server.getDataCenter().runTransact(transaction -> {
            clan.setName(transaction, args[0]);
            helper.sendClanMsg(server, clan,
                    helper.replaceText(commandConfig.renamed(),
                            Map.of("{player}", player.getName(),
                                    "{name}", args[0])));
        }).exceptionally(ex -> {
            player.sendMessage(pluginConfig.error());
            helper.getLogger().error("Something went wrong renaming a clan", ex);
            return null;
        });
    }

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getArgs() {
        return "[Name]";
    }

    @Override
    public String getDescription() {
        return "Renames the clan";
    }
}
