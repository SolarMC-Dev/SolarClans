package gg.solarmc.clans.command;

import gg.solarmc.clans.helper.PluginHelper;
import org.bukkit.command.CommandSender;

public interface SubCommand {

    void execute(CommandSender sender, String[] args, PluginHelper helper);

    String getName();

    String getArgs();

    String getDescription();

}
