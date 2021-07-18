package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.loader.DataCenter;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements ClansSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return;
        }

        if (args.length == 0) {
            Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
            if (clan == null) {
                helper.sendNotInClanMsg(player);
                return;
            }

            sendClanInfo(player, clan);
            return;
        }

        DataCenter dataCenter = sender.getServer().getDataCenter();
        dataCenter.runTransact(transaction -> {
            int name = 0; // Make this args[0]
            Clan clan = dataCenter.getDataManager(ClansKey.INSTANCE).getClan(transaction, name);

            sendClanInfo(player, clan);
        }).exceptionally((ex) -> {
            player.sendMessage(ChatColor.RED + "Something went wrong! Please try again Later");
            return null;
        });
    }

    private void sendClanInfo(Player player, Clan clan) {
        String clanName = clan.getName();
        String allyClanName = clan.currentAllyClan().map(Clan::getName).orElse("No Ally");
        int kills = clan.currentKills();
        int assists = clan.currentAssists();
        int deaths = clan.currentDeaths();
        // TODO
        // final Set<ClanMember> clanMembers = clan.currentMembers();

        String[] info = {
                "Information about " + clanName,
                "Kills : " + kills,
                "Assists : " + assists,
                "Deaths : " + deaths,
                "Ally : " + allyClanName,
                // "Members : "
        };
        // TODO : make it look good
        player.sendMessage(info);
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
