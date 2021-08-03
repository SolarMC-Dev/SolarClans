import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

interface Config {
    static TextComponent yes() {
        return Component.text("HELLO", NamedTextColor.GOLD).append(Component.text("BYE", NamedTextColor.RED));
    }

    @ConfKey("component")
    @ConfDefault.DefaultObject("yes")
    TextComponent component();
}
