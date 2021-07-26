package gg.solarmc.clans.command.commands.ally;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;

import java.util.List;

public class AllyCommand implements PluginCommand {
    private final List<SubCommand> subCommands;

    public AllyCommand(SolarClans plugin) {
        this.subCommands = List.of(
                new AddCommand(),
                new RemoveCommand()
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
