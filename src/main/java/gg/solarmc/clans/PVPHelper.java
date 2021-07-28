package gg.solarmc.clans;

import gg.solarmc.loader.clans.Clan;

import java.util.ArrayList;
import java.util.List;

public class PVPHelper {
    private final List<Integer> enabledPvp = new ArrayList<>();

    public boolean isPvpOn(Clan clan) {
        return enabledPvp.contains(clan.getID());
    }

    public void setPvp(Clan clan, boolean pvpOn) {
        if (pvpOn) enabledPvp.add(clan.getID());
        else enabledPvp.remove(Integer.valueOf(clan.getID()));
    }
}
