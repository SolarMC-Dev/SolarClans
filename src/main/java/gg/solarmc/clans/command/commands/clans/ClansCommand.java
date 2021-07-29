package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.PluginCommand;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;

import java.util.List;

public class ClansCommand implements PluginCommand {
    private final List<SubCommand> subCommands;
    private final PluginHelper commandHelper;

    public ClansCommand(SolarClans plugin, PluginHelper commandHelper, PVPHelper pvpHelper, ChatHelper chatHelper) {
        this.commandHelper = commandHelper;
        this.subCommands = List.of(
                new ChatCommand(chatHelper, ChatMode.CLAN),
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
    public PluginHelper getHelper() {
        return commandHelper;
    }
}
