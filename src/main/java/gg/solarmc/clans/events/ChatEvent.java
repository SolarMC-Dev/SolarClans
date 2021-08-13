package gg.solarmc.clans.events;

import gg.solarmc.clans.ChatMode;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

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
        Server server = player.getServer();
        switch (chatMode) {
            case CLAN -> {
                Component msg = pluginHelper.replaceText(plugin.getPluginConfig().chatFormat().clan(),
                        Map.of("{player}", player.getName(),
                                "{message}", message));
                pluginHelper.sendClanMsg(server, clan, msg);
            }
            case ALLY -> {
                Clan allyClan = clan.currentAllyClan().orElse(null);
                if (allyClan == null) {
                    player.sendMessage(Component.text("Your clan doesn't have a ally", NamedTextColor.RED)
                            .append(Component.newline())
                            .append(Component.text("Switching your Chat to NORMAL")));
                    chatHelper.setChatMode(player, NORMAL);
                    return;
                }

                Component msg = pluginHelper.replaceText(plugin.getPluginConfig().chatFormat().ally(),
                        Map.of("{clan}", clan.currentClanName(),
                                "{player}", player.getName(),
                                "{message}", message));
                pluginHelper.sendClanMsg(server, clan, msg);
                pluginHelper.sendClanMsg(server, allyClan, msg);
            }
        }
    }
}
