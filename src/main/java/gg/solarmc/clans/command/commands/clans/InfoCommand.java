package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.SolarPlayer;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.arim.omnibus.util.concurrent.CentralisedFuture;

import java.util.Map;

public class InfoCommand implements SubCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(InfoCommand.class);
    private final SolarClans plugin;

    public InfoCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command, If you want clan info use '/clan info [Clan Name]'!");
                return;
            }
        }

        DataCenter dataCenter = sender.getServer().getDataCenter();
        MessageConfig pluginConfig = plugin.getPluginConfig();
        dataCenter.runTransact(transaction -> {
            if (args.length == 0) {
                Player player = (Player) sender;

                Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).getClan(transaction).orElse(null);
                if (clan == null) {
                    helper.sendNotInClanMsg(player);
                    return;
                }
                sendClanInfo(player, clan, helper);
                return;
            }

            Clan clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClanByName(transaction, args[0]).orElse(null);

            if (clan == null) {
                sender.sendMessage(pluginConfig.clanNotExist());
                return;
            }

            sendClanInfo(sender, clan, helper);
        }).exceptionally((ex) -> {
            sender.sendMessage(pluginConfig.error());
            LOGGER.error("Something went wrong getting info about a clan", ex);
            return null;
        });
    }

    private void sendClanInfo(CommandSender player, Clan clan, PluginHelper helper) {
        String clanName = clan.currentClanName();
        String allyClanName = clan.currentAllyClan().map(Clan::currentClanName).orElse("No Ally");
        int kills = clan.currentKills();
        int assists = clan.currentAssists();
        int deaths = clan.currentDeaths();
        StringBuilder members = new StringBuilder();


        CentralisedFuture[] memberFutures = (CentralisedFuture[]) clan.currentMembers().stream().map(it ->
                getPlayerNameById(player.getServer(), it.userId())
                        .thenAccept(a -> members.append(a).append(",")))
                .toArray();

        helper.futuresFactory(player.getServer()).allOf(memberFutures).thenRunSync(() -> {
            Component info = helper.replaceText(plugin.getPluginConfig().clan().info(),
                    Map.of("{clan}", clanName,
                            "{kills}", String.valueOf(kills),
                            "{assists}", String.valueOf(assists),
                            "{deaths}", String.valueOf(deaths),
                            "{ally}", allyClanName,
                            "{members}", members.toString()));

            player.sendMessage(info);
        });
    }

    private CentralisedFuture<String> getPlayerNameById(Server server, int id) {
        return server.getDataCenter().lookupPlayer(id).thenApplySync(o -> o.orElse(null))
                .thenApplySync(SolarPlayer::getMcUsername)
                .exceptionally(e -> {
                    LOGGER.error("Cannot lookup player by id", e);
                    return null;
                });
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getArgs() {
        return "[Clan Name/Optional]";
    }

    @Override
    public String getDescription() {
        return "Shows you the information about the clan with the name you typed";
    }
}
