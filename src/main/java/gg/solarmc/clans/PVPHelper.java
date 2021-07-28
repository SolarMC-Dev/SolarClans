package gg.solarmc.clans;

import gg.solarmc.loader.clans.Clan;

import java.util.ArrayList;
import java.util.List;

public class PVPHelper {
    private final List<Integer> disabledPVP = new ArrayList<>();

    public boolean isPvpOn(Clan clan) {
        return !disabledPVP.contains(clan.getID());
    }

    public void setPvp(Clan clan, boolean pvpOn) {
        if (pvpOn) disabledPVP.remove(Integer.valueOf(clan.getID()));
        else disabledPVP.add(clan.getID());
    }
}
