import net.kyori.adventure.text.TextComponent;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;

interface Config {
    @ConfKey("component")
    @ConfDefault.DefaultString("&6HELLO &cBYE")
    TextComponent component();
}
