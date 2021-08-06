package gg.solarmc.clans.events;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static gg.solarmc.clans.ChatMode.NORMAL;

public class ChatEvent implements Listener {
    private final ChatHelper chatHelper;
    private final PluginHelper pluginHelper;

    public ChatEvent(PluginHelper pluginHelper, ChatHelper chatHelper) {
        this.chatHelper = chatHelper;
        this.pluginHelper = pluginHelper;
    }

    // If you use DeluxeChat , it uses HIGHEST priority :D
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        if (clan == null) return;

        ChatMode chatMode = chatHelper.getChatMode(player);
        event.setCancelled(true);

        String message = event.getMessage();
        switch (chatMode) {
            case NORMAL -> {
                player.getServer().getDataCenter().runTransact(transaction -> {
                    TextComponent msg = Component.text("[" + clan.getClanName(transaction) + "]")
                            .append(Component.text("<" + player.getName() + "> "))
                            .append(Component.text(message));

                    event.getRecipients().forEach(p -> p.sendMessage(msg));
                });
            }
            case CLAN -> pluginHelper.sendPlayerClanMsg(player.getServer(), player, clan, "Clan", message);
            case ALLY -> {
                Clan allyClan = clan.currentAllyClan().orElse(null);
                if (allyClan == null) {
                    player.sendMessage(Component.text("Your clan doesn't have a ally", NamedTextColor.RED)
                            .append(Component.newline())
                            .append(Component.text("Switching your Chat to NORMAL")));
                    chatHelper.setChatMode(player, NORMAL);
                    return;
                }

                pluginHelper.sendPlayerClanMsg(player.getServer(), player, clan, "Ally", message);
                pluginHelper.sendPlayerClanMsg(player.getServer(), player, allyClan, "Ally", message);
            }
        }
    }
}
