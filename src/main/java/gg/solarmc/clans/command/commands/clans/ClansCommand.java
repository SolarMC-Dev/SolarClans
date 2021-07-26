package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;

import java.util.List;

public class ClansCommand implements PluginCommand {
    private final List<SubCommand> subCommands;

    public ClansCommand(SolarClans plugin) {
        this.subCommands = List.of(
                new CreateCommand(plugin),
                new DisbandCommand(),
                new InfoCommand(),
                new InviteCommand(),
                new JoinCommand(),
                new KickCommand(),
                new PvpCommand(),
                new RenameCommand(),
                new SetLeaderCommand(),
                new VaultCommand()
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
