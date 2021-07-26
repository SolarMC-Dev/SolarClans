package gg.solarmc.clans.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PluginCommand extends CommandExecutor {

    List<SubCommand> getSubCommands();

    @Override
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            String commands = "Clans Commands : \n" +
                    getSubCommands().stream()
                            .map(it -> ChatColor.BOLD + it.getName() + ChatColor.RESET
                                    + " " + it.getArgs()
                                    + " : " + it.getDescription())
                            .collect(Collectors.joining("\n"));
            sender.sendMessage(commands.split("\n"));
            return true;
        }

        String arg = args[0];
        SubCommand subCommand = getCommand(arg);

        if (subCommand != null) {
            List<String> subArgs = Arrays.asList(args).subList(1, args.length);

            subCommand.execute(sender, subArgs.toArray(String[]::new), new CommandHelper());
            return true;
        } else
            sender.sendMessage("No Command for " + arg + " in credits");

        return true;
    }

    private SubCommand getCommand(String name) {
        return getSubCommands().stream()
                .filter(cmd -> cmd.getName().equals(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
