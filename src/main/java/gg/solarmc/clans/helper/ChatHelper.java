package gg.solarmc.clans.helper;

import gg.solarmc.clans.ChatMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatHelper {
    private final Map<UUID, ChatMode> chatMode = new HashMap<>();

    public ChatMode getChatMode(Player player) {
        return chatMode.computeIfAbsent(player.getUniqueId(), it -> ChatMode.NORMAL);
    }

    public void setChatMode(Player player, ChatMode mode) {
        chatMode.put(player.getUniqueId(), mode);
    }

}
