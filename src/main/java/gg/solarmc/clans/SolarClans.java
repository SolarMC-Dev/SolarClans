package gg.solarmc.clans;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import gg.solarmc.clans.command.commands.ally.AllyCommand;
import gg.solarmc.clans.command.commands.clans.ClansCommand;
import gg.solarmc.clans.config.ConfigManager;
import gg.solarmc.clans.config.MessageConfig;
import gg.solarmc.clans.events.HitEvent;
import gg.solarmc.clans.events.StatsEvent;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolarClans extends JavaPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolarClans.class);
    private Economy econ;
    private WorldGuardPlugin worldGuard;
    private ConfigManager<MessageConfig> config;

    @Override
    public void onEnable() {
        /*if (!setupEconomy()) {
            LOGGER.error(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            setEnabled(false);
            return;
        }*/
        worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

        if (!getServer().getPluginManager().isPluginEnabled("PlayerVaults")) {
            getLogger().severe("PlayerVaults is not installed or not enabled. Disabling this Plugin :(");
            setEnabled(false);
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        //HDPlaceholders placeholders = new HDPlaceholders(this);
        //placeholders.registerPlaceHolders();

        PluginHelper helper = new PluginHelper(getServer());
        PVPHelper clanPvpHelper = new PVPHelper();
        PVPHelper allyPvpHelper = new PVPHelper();
        ChatHelper chatHelper = new ChatHelper();

        config = ConfigManager.create(this.getDataFolder().toPath(), "msgconfig.yml", MessageConfig.class);
        config.reloadConfig();

        // Commands
        getServer().getPluginCommand("clans").setExecutor(new ClansCommand(this, helper, clanPvpHelper, chatHelper));
        getServer().getPluginCommand("ally").setExecutor(new AllyCommand(this, helper, allyPvpHelper, chatHelper));

        // Events
        getServer().getPluginManager().registerEvents(new HitEvent(this, helper, clanPvpHelper, allyPvpHelper), this);
        getServer().getPluginManager().registerEvents(new StatsEvent(), this);
    }

    @Override
    public void onDisable() {
        LOGGER.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    public MessageConfig getPluginConfig() {
        return config.getConfigData();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public WorldGuardPlugin getWorldGuardManager() {
        return worldGuard;
    }
}
