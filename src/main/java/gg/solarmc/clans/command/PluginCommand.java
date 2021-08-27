package gg.solarmc.clans.command;

import gg.solarmc.clans.helper.PluginHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PluginCommand extends CommandExecutor {

    List<SubCommand> getSubCommands();

    String getName();

    PluginHelper getHelper();

    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = getName();
        if (args.length == 0) {
            String commands = name + " Commands : \n" +
                    getSubCommands().stream()
                            .map(it -> ChatColor.BOLD + it.getName() + ChatColor.RESET
                                    + " " + it.getArgs()
                                    + " : " + it.getDescription())
                            .collect(Collectors.joining("\n"));
            sender.sendMessage(commands.split("\n"));
            return true;
        }

        String arg = args[0];
        if (label.equalsIgnoreCase("ally")
                && !arg.equalsIgnoreCase("add")
                && !arg.equalsIgnoreCase("remove")
                && !arg.equalsIgnoreCase("chat")
                && !arg.equalsIgnoreCase("pvp")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "This command can be only used by Players!");
                return true;
            }
            if (args.length <= 1) player.performCommand("ally add");
            else player.performCommand("ally add " + args[0]);
            return true;
        }

        SubCommand subCommand = getCommand(arg);

        if (subCommand != null) {
            List<String> subArgs = Arrays.asList(args).subList(1, args.length);

            subCommand.execute(sender, subArgs.toArray(String[]::new), getHelper());
            return true;
        } else
            sender.sendMessage("No Command for " + arg + " in " + name);

        return true;
    }


    private SubCommand getCommand(String name) {
        return getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
