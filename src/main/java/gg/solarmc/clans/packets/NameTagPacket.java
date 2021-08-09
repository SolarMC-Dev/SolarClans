package gg.solarmc.clans.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import gg.solarmc.clans.SolarClans;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class NameTagPacket {
    private final PacketType TYPE = PacketType.Play.Server.TAGS;
    private final Logger LOGGER = LoggerFactory.getLogger(NameTagPacket.class);

    private final SolarClans plugin;

    public NameTagPacket(SolarClans plugin) {
        this.plugin = plugin;
    }

    public void addPacket() {
        ProtocolManager protocolManager = plugin.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, TYPE) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() != TYPE) return;
                        event.setCancelled(true);

                        PacketContainer packet = event.getPacket();
                        Entity entity = packet.getEntityModifier(event).read(0);
                        if (!(entity instanceof Player other)) return;

                        Player player = event.getPlayer();

                        Clan playerClan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
                        Clan otherClan = other.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

                        if (playerClan != null && otherClan != null) {
                            if (playerClan.equals(otherClan)) {
                                other.displayName(other.displayName().color(NamedTextColor.GREEN));
                            }
                            else if (otherClan.equals(playerClan.currentAllyClan().orElse(null))) {
                                other.displayName(other.displayName().color(NamedTextColor.LIGHT_PURPLE));
                            }
                            // TODO:
                            // else if(otherClan.equals(playerClan.currentEnemyClan().orElse(null))) {other.displayName(other.displayName().color(NamedTextColor.RED));}
                        }

                        packet.getEntityModifier(event).write(0, other);
                        try {
                            protocolManager.sendServerPacket(player, packet);
                        } catch (InvocationTargetException e) {
                            LOGGER.error("Couldn't send the packet :(", e);
                        }
                    }
                }
        );
    }

}
