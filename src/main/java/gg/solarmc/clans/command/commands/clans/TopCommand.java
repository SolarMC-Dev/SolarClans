package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.clan.ClanTopConfig;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.ClanManager;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TopCommand implements SubCommand {
    private final SolarClans plugin;

    public TopCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        MessageConfig pluginConfig = plugin.getPluginConfig();
        ClanTopConfig commandConfig = pluginConfig.clan().top();

        if (helper.invalidateArgs(sender, args, commandConfig.invalidArgs())) return;

        int amount = 5;

        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
        }

        DataCenter dataCenter = sender.getServer().getDataCenter();
        ClanManager manager = dataCenter.getDataManager(ClansKey.INSTANCE);

        int finalAmount = amount;

        dataCenter.runTransact(transaction -> {
            int count = 1;
            Component leaderboard = commandConfig.leaderboard();

            AtomicReference<List<ClanManager.TopClanResult>> result = new AtomicReference<>();

            switch (args[0]) {
                case "kill", "kills" -> result.set(manager.getTopClanKills(transaction, finalAmount));
                case "assist", "assists" -> result.set(manager.getTopClanAssists(transaction, finalAmount));
                case "death", "deaths" -> result.set(manager.getTopClanDeaths(transaction, finalAmount));
                case "bal", "balance" -> {

                }
                default -> sender.sendMessage(commandConfig.invalidOption());
            }

            leaderboard = leaderboard.append(Component.text(StringUtils.capitalize(args[0]), NamedTextColor.GOLD));

            for (ClanManager.TopClanResult it : result.get()) {
                Component component = helper.replaceText(commandConfig.clanFormat(),
                        Map.of("{number}", String.valueOf(count),
                                "{clan}", it.clanName(),
                                "{value}", String.valueOf(it.statisticValue())));
                leaderboard = leaderboard.append(component).append(Component.newline());
                count++;
            }

            sender.sendMessage(leaderboard);
        });
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getArgs() {
        return "[kills|assists|deaths|balance] [number/Optional]";
    }

    @Override
    public String getDescription() {
        return "Shows you the Top 5 Clans";
    }
}
