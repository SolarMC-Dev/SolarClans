import gg.solarmc.clans.config.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class ConfigTest {
    @Test
    void createCorrectConfig() {
        ConfigManager<Config> manager = ConfigManager.create(Path.of("src", "test", "resources"), "config.yml", Config.class);
        manager.reloadConfig();

        Assertions.assertEquals(
                Component.text("HELLO", NamedTextColor.GOLD).append(Component.text("BYE", NamedTextColor.RED)),
                manager.getConfigData().component());
    }
}
