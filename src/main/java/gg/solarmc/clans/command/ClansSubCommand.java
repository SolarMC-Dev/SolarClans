package gg.solarmc.clans.command;

import org.bukkit.command.CommandSender;

public interface ClansSubCommand {

    void execute(CommandSender sender, String[] args, CommandHelper helper);

    String getName();

    String getArgs();

    String getDescription();

}
