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
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AllyCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final PluginHelper commandHelper;
    private final AddCommand addCommand;

    public AllyCommand(SolarClans plugin, PluginHelper commandHelper, PVPHelper pvpHelper, ChatHelper chatHelper) {
        addCommand = new AddCommand(plugin);
        this.commandHelper = commandHelper;
        this.subCommands = List.of(
                addCommand,
                new RemoveCommand(plugin),
                new PVPCommand(plugin, pvpHelper),
                new ChatCommand(plugin, chatHelper, ChatMode.ALLY)
        );
    }

    @Override
    public boolean beforeCommand(CommandSender sender, String command, String[] args) {
        if (command.equalsIgnoreCase("ally") && args[0] != null) {
            String[] strings = Arrays.stream(args).toList().subList(1, args.length).toArray(String[]::new);
            addCommand.execute(sender, strings, commandHelper);
            return true;
        }
        return false;
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
}
