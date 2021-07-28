package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.PVPHelper;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.PluginHelper;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.command.commands.clans.PVPCommand;

import java.util.List;

public class AllyCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final PluginHelper commandHelper;

    public AllyCommand(SolarClans plugin, PluginHelper commandHelper, PVPHelper pvpHelper) {
        this.commandHelper = commandHelper;
        this.subCommands = List.of(
                new AddCommand(),
                new RemoveCommand(),
                new PVPCommand(pvpHelper)
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public PluginHelper getHelper() {
        return commandHelper;
    }
}
