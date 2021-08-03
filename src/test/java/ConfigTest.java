import gg.solarmc.clans.config.ConfigManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class ConfigTest {
    @Test
    void createCorrectConfig() {
        ConfigManager<Config> manager = ConfigManager.create(Path.of("src", "test", "resources"), "config.yml", Config.class);
        manager.reloadConfig();

        Assertions.assertEquals(Config.yes(), manager.getConfigData().component());
    }
}
