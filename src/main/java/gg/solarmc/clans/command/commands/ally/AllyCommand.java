package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.command.commands.clans.ChatCommand;
import gg.solarmc.clans.command.commands.clans.PVPCommand;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AllyCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final PluginHelper commandHelper;

    private final AddCommand addCommand;

    public AllyCommand(SolarClans plugin) {
        this.commandHelper = plugin.getHelper();
        PVPHelper pvpHelper = plugin.getAllyPvpHelper();
        ChatHelper chatHelper = plugin.getChatHelper();

        addCommand = new AddCommand(plugin);
        this.subCommands = List.of(
                addCommand,
                new RemoveCommand(plugin),
                new PVPCommand(plugin, pvpHelper),
                new ChatCommand(plugin, chatHelper, ChatMode.ALLY)
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public String getName() {
        return "Ally";
    }

    @Override
    public PluginHelper getHelper() {
        return commandHelper;
    }

    @Override
    public boolean beforeCommand(CommandSender sender, String label, String[] args) {
        if (!args[0].equalsIgnoreCase("add")
                && !args[0].equalsIgnoreCase("remove")
                && !args[0].equalsIgnoreCase("chat")
                && !args[0].equalsIgnoreCase("pvp")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can be only used by Players!");
                return true;
            }
            addCommand.execute(sender, args, commandHelper);
            return true;
        }
        return false;
    }
}
