package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.MessageConfig;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand implements SubCommand {
    private final SolarClans plugin;

    public RenameCommand(SolarClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;
        if (helper.invalidateArgs(sender, args,
                ChatColor.RED + "You need to specify the Name of the Clan!!")) return;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        MessageConfig config = plugin.getPluginConfig();

        if (!helper.isLeader(clan, player)) {
            player.sendMessage(config.leaderCommand());
            return;
        }

        final Server server = player.getServer();

        server.getDataCenter().runTransact(transaction -> {
            clan.setName(transaction, args[0]);
            helper.sendClanMsg(server, clan,
                    Component.text(player.getName() + " renamed the clan to ", NamedTextColor.GREEN)
                            .append(Component.text(args[0], NamedTextColor.GOLD)));
        }).exceptionally(ex -> {
            player.sendMessage(ChatColor.RED + "Couldn't rename the clan! Something went wrong, Please try again later!!");
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
