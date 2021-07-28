package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.PVPHelper;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.CommandHelper;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;

import java.util.List;

public class ClansCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final CommandHelper commandHelper;

    public ClansCommand(SolarClans plugin, CommandHelper commandHelper, PVPHelper pvpHelper) {
        this.commandHelper = commandHelper;
        this.subCommands = List.of(
                new CreateCommand(plugin),
                new DisbandCommand(),
                new InfoCommand(),
                new InviteCommand(),
                new JoinCommand(),
                new KickCommand(),
                new PVPCommand(pvpHelper),
                new RenameCommand(),
                new SetLeaderCommand(),
                new VaultCommand()
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public CommandHelper getHelper() {
        return commandHelper;
    }
}
