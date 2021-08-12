package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements SubCommand {
    private final SolarClans plugin;
    private final ChatHelper chatHelper;
    private final ChatMode mode;

    public ChatCommand(SolarClans plugin, ChatHelper chatHelper, ChatMode mode) {
        this.plugin = plugin;
        this.chatHelper = chatHelper;
        this.mode = mode;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;

        Player player = (Player) sender;
        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        MessageConfig pluginConfig = plugin.getPluginConfig();

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (chatHelper.getChatMode(player) == ChatMode.NORMAL)
            chatHelper.setChatMode(player, mode);
        else
            chatHelper.setChatMode(player, ChatMode.NORMAL);

        player.sendMessage(helper.replaceText(pluginConfig.chatToggled(), "{chatmode}", chatHelper.getChatMode(player).name()));
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public String getArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Toggles to Clan Chat";
    }
}
