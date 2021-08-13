package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.clan.ClanJoinConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements SubCommand {

    private final SolarClans plugin;

    public JoinCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;

        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanJoinConfig commandConfig = pluginConfig.clan().join();

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;
        Player player = (Player) sender;

        Server server = player.getServer();

        DataCenter dataCenter = server.getDataCenter();
        dataCenter.runTransact(transaction -> {
            Clan clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if (clan == null) {
                sender.sendMessage(pluginConfig.clanNotExist());
                return;
            }

            if (!helper.hasInvited(clan, player)) {
                player.sendMessage(commandConfig.notInvited());
                return;
            }

            clan.addClanMember(transaction, player.getSolarPlayer());
            helper.sendClanMsg(server, clan,
                    helper.replaceText(commandConfig.joined(), "{player}", player.getName()));
            helper.removeInvite(clan, player);
        }).exceptionally((ex) -> {
            player.sendMessage(pluginConfig.error());
            return null;
        });

    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getArgs() {
        return "[Clan Name]";
    }

    @Override
    public String getDescription() {
        return "Joins the clan with the name You invited";
    }
}
