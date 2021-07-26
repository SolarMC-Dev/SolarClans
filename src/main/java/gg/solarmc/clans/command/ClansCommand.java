package gg.solarmc.clans.command;

import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.commands.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClansCommand implements CommandExecutor {
    private final SolarClans plugin;
    private final List<ClansSubCommand> subCommands;

    public ClansCommand(SolarClans plugin) {
        this.plugin = plugin;
        this.subCommands = List.of(
                new AllyCommand(),
                new AllyRemoveCommand(),
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            String commands = "Clans Commands : \n" +
                    subCommands.stream()
                            .map(it -> ChatColor.BOLD + it.getName() + ChatColor.RESET
                                    + " " + it.getArgs()
                                    + " : " + it.getDescription())
                            .collect(Collectors.joining("\n")) +
                    "\n/clans <command>";
            sender.sendMessage(commands.split("\n"));
            return true;
        }

        String arg = args[0];
        ClansSubCommand subCommand = getCommand(arg);

        if (subCommand != null) {
            List<String> subArgs = Arrays.asList(args).subList(1, args.length);

            subCommand.execute(sender, subArgs.toArray(String[]::new), new CommandHelper());
            return true;
        } else
            sender.sendMessage("No Command for " + arg + " in credits");

        return true;
    }

    private ClansSubCommand getCommand(String name) {
        return this.subCommands.stream()
                .filter(cmd -> cmd.getName().equals(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
