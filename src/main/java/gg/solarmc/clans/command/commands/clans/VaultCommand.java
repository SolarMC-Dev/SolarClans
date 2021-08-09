package gg.solarmc.clans.command.commands.clans;

import com.drtshock.playervaults.PlayerVaults;
import com.drtshock.playervaults.vaultmanagement.VaultOperations;
import com.drtshock.playervaults.vaultmanagement.VaultViewInfo;
import gg.solarmc.clans.command.SubCommand;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.loader.clans.Clan;
import gg.solarmc.loader.clans.ClansKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VaultCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args, PluginHelper helper) {
        if (helper.invalidateCommandSender(sender)) return;
        Player player = (Player) sender;

        Clan clan = player.getSolarPlayer().getData(ClansKey.INSTANCE).currentClan().orElse(null);

        if (clan == null) {
            helper.sendNotInClanMsg(player);
            return;
        }

        if (PlayerVaults.getInstance().getInVault().containsKey(player.getUniqueId().toString()))
            return; // Already in a vault so they must be trying to dupe.

        String vaultName = "clan_vault:" + clan.getClanId();

        if (VaultOperations.openOtherVault(player, vaultName, String.valueOf(1), false)) {
            // Success
            PlayerVaults.getInstance().getInVault().put(player.getUniqueId().toString(),
                    new VaultViewInfo(vaultName, 1));
        }
    }

    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public String getArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Opens the Clan Vault";
    }
}
