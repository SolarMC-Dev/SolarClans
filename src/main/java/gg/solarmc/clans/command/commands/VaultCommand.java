package gg.solarmc.clans.command.commands;

import gg.solarmc.clans.command.ClansSubCommand;
import gg.solarmc.clans.command.CommandHelper;
import org.bukkit.command.CommandSender;

public class VaultCommand implements ClansSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, CommandHelper helper) {

    }

    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public String getArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Opens the Clan Vault";
    }
}
