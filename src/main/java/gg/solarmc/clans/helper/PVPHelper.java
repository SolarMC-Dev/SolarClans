package gg.solarmc.clans.helper;

import gg.solarmc.loader.clans.Clan;

import java.util.ArrayList;
import java.util.List;

public class PVPHelper {
    private final List<Integer> enabledPvp = new ArrayList<>();

    public boolean isPvpOn(Clan clan) {
        return enabledPvp.contains(clan.getClanId());
    }

    public void setPvp(Clan clan, boolean pvpOn) {
        if (pvpOn) enabledPvp.add(clan.getClanId());
        else enabledPvp.remove(Integer.valueOf(clan.getClanId()));
    }
}
