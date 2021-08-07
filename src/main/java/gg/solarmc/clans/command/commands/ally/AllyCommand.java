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

import java.util.List;

public class AllyCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final PluginHelper commandHelper;

    public AllyCommand(SolarClans plugin, PluginHelper commandHelper, PVPHelper pvpHelper, ChatHelper chatHelper) {
        this.commandHelper = commandHelper;
        this.subCommands = List.of(
                new AddCommand(plugin),
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
}
