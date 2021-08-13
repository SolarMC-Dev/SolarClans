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

    public ClansCommand(SolarClans plugin) {
        this.commandHelper = plugin.getHelper();
        PVPHelper pvpHelper = plugin.getClanPvpHelper();
        ChatHelper chatHelper = plugin.getChatHelper();

        this.subCommands = List.of(
                new ChatCommand(plugin, chatHelper, ChatMode.CLAN),
                new CreateCommand(plugin),
                new DisbandCommand(plugin),
                new InfoCommand(plugin),
                new InviteCommand(plugin),
                new JoinCommand(plugin),
                new KickCommand(plugin),
                new LeaveCommand(plugin),
                new PVPCommand(plugin, pvpHelper),
                new RenameCommand(plugin),
                new SetLeaderCommand(plugin),
                new TopCommand(plugin),
                new VaultCommand()
        );
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public String getName() {
        return "Clans";
    }

    @Override
    public PluginHelper getHelper() {
        return commandHelper;
    }
}
