import gg.solarmc.clans.config.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    @Test
    void createCorrectConfig() {
        ConfigManager<Config> manager = ConfigManager.create(Path.of("src", "test", "resources"), "config.yml", Config.class);
        manager.reloadConfig();

        TextComponent expected = Component.text("HELLO ", NamedTextColor.GOLD).append(Component.text("BYE", NamedTextColor.RED));
        TextComponent actual = manager.getConfigData().component();
        assertEquals(legacyText(expected), legacyText(actual));
    }

    String legacyText(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }
}
