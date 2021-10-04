package gg.solarmc.clans.command;

import gg.solarmc.clans.helper.PluginHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PluginCommand extends CommandExecutor {

    List<SubCommand> getSubCommands();

    String getName();

    PluginHelper getHelper();

    default boolean beforeCommand(CommandSender sender, String label, String[] args) {
        return false;
    }

    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = getName();
        if (args.length == 0) {
            TextComponent commands = Component.join(Component.text(", ", NamedTextColor.DARK_AQUA),
                    getSubCommands().stream().map(it ->
                            Component.text(it.getName(), Style.style(TextDecoration.BOLD))
                                    .append(Component.text(" " + it.getArgs()))
                                    .hoverEvent(HoverEvent.showText(Component.text(it.getDescription(), NamedTextColor.AQUA)))
                    ).collect(Collectors.toList()));

            TextComponent menu = Component.text(name + " Commands : ")
                    .append(Component.newline())
                    .append(commands);

            sender.sendMessage(menu);
            return true;
        }

        if (beforeCommand(sender, label, args)) return true;

        String arg = args[0];

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
