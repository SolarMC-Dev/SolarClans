package gg.solarmc.clans.helper;

import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.entity.Player;

// Helper for other plugins to use Clans
public class ClanHelper {

    public ClanRelation getRelation(Player player, Player other) {
        Clan playerClan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);
        Clan otherClan = other.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (playerClan != null && otherClan != null) {
            if (playerClan.equals(otherClan))
                return ClanRelation.MEMBER;
            else if (otherClan.equals(playerClan.currentAllyClan().orElse(null)))
                return ClanRelation.ALLY;
            // TODO:
            // else if(otherClan.equals(playerClan.currentEnemyClan().orElse(null)))
            //  return ClanRelation.ENEMY;
        }

        return ClanRelation.NEUTRAL;
    }

}
