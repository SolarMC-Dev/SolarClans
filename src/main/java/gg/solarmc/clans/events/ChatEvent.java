package gg.solarmc.clans.events;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static gg.solarmc.clans.ChatMode.NORMAL;

public class ChatEvent implements Listener {
    private final SolarClans plugin;

    public ChatEvent(SolarClans plugin) {
        this.plugin = plugin;
    }

    // If you use DeluxeChat , it uses HIGHEST priority :D
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        if (clan == null) return;

        PluginHelper pluginHelper = plugin.getHelper();
        ChatHelper chatHelper = plugin.getChatHelper();

        ChatMode chatMode = chatHelper.getChatMode(player);
        if (chatMode == NORMAL) return;

        event.setCancelled(true);

        String message = event.getMessage();
        switch (chatMode) {
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
