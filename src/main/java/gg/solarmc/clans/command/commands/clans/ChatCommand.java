package gg.solarmc.clans.command.commands.clans;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements SubCommand {
    private final ChatHelper chatHelper;
    private final ChatMode mode;

    public ChatCommand(ChatHelper chatHelper, ChatMode mode) {
        this.chatHelper = chatHelper;
        this.mode = mode;
    }

    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender, args)) return;

        Player player = (Player) sender;
        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (chatHelper.getChatMode(player) != ChatMode.NORMAL)
            chatHelper.setChatMode(player, mode);
        else chatHelper.setChatMode(player, ChatMode.NORMAL);

        player.sendMessage(Component.text("Your Chat has been Toggled to " + chatHelper.getChatMode(player), NamedTextColor.GREEN));
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
