package gg.solarmc.clans.config;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

public class TextComponentSerializer implements ValueSerialiser<TextComponent> {
    @Override
    public Class<TextComponent> getTargetClass() {
        return TextComponent.class;
    }

    @Override
    public TextComponent deserialise(FlexibleType flexibleType) throws BadValueException {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(flexibleType.getString());
    }

    @Override
    public Object serialise(TextComponent value, Decomposer decomposer) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(value);
    }
}
