package gg.solarmc.clans;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import gg.solarmc.clans.command.commands.ally.AllyCommand;
import gg.solarmc.clans.command.commands.clans.ClansCommand;
import gg.solarmc.clans.config.configs.MessageConfig;
import gg.solarmc.clans.config.manager.ConfigManager;
import gg.solarmc.clans.events.ChatEvent;
import gg.solarmc.clans.events.HitEvent;
import gg.solarmc.clans.events.StatsEvent;
import gg.solarmc.clans.helper.ChatHelper;
import gg.solarmc.clans.helper.PVPHelper;
import gg.solarmc.clans.helper.PluginHelper;
import gg.solarmc.clans.placeholder.ClansRelationColor;
import gg.solarmc.clans.placeholder.HDPlaceholders;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import subside.plugins.koth.KothPlugin;

public class SolarClans extends JavaPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolarClans.class);
    private ConfigManager<MessageConfig> config;

    private Economy economy;
    private WorldGuardPlugin worldGuard;
    private KothPlugin koth;

    private PluginHelper helper = new PluginHelper(this);
    private PVPHelper clanPvpHelper = new PVPHelper();
    private PVPHelper allyPvpHelper = new PVPHelper();
    private ChatHelper chatHelper = new ChatHelper();

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            LOGGER.error(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            setEnabled(false);
            return;
        }

        Plugin worldGuardPlugin = setupPlugin("WorldGuard");
        this.worldGuard = (WorldGuardPlugin) worldGuardPlugin;

        setupPlugin("PlayerVaults");
        setupPlugin("HolographicDisplays");
        setupPlugin("PlaceholderAPI");

        Plugin kothPlugin = setupPlugin("KoTH");
        this.koth = (KothPlugin) kothPlugin;

        HDPlaceholders placeholders = new HDPlaceholders(this);
        placeholders.registerPlaceHolders();

        // Placeholder
        new ClansRelationColor(this).register();

        helper = new PluginHelper(this);
        clanPvpHelper = new PVPHelper();
        allyPvpHelper = new PVPHelper();
        chatHelper = new ChatHelper();

        config = ConfigManager.create(this.getDataFolder().toPath(), "config.yml", MessageConfig.class);
        config.reloadConfig();

        // Commands
        getServer().getPluginCommand("clans").setExecutor(new ClansCommand(this));
        getServer().getPluginCommand("ally").setExecutor(new AllyCommand(this));

        // Events
        getServer().getPluginManager().registerEvents(new HitEvent(this), this);
        getServer().getPluginManager().registerEvents(new StatsEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
    }

    @Override
    public void onDisable() {
        LOGGER.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    private Plugin setupPlugin(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        if (plugin == null) {
            LOGGER.warn("*** {} is not installed or not enabled. ***", name);
            throw new IllegalStateException("*** This plugin will be disabled. ***");
        }

        return plugin;
    }

    public MessageConfig getPluginConfig() {
        return config.getConfigData();
    }

    // Hooks

    private boolean setupEconomy() {
        setupPlugin("Vault");

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public KothPlugin getKoth() {
        return koth;
    }

    public WorldGuardPlugin getWorldGuardManager() {
        return worldGuard;
    }

    // Helpers

    public PluginHelper getHelper() {
        return helper;
    }

    public PVPHelper getClanPvpHelper() {
        return clanPvpHelper;
    }

    public PVPHelper getAllyPvpHelper() {
        return allyPvpHelper;
    }

    public ChatHelper getChatHelper() {
        return chatHelper;
    }

}
