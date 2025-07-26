package dev.metallurgists.metallurgica.registry;

import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.util.ForgeSoundType;

public class MetallurgicaSoundTypes {

    public static final ForgeSoundType COAL;


    static {
        COAL  = new ForgeSoundType(1.0F, 1.0F, () -> SoundEvents.GILDED_BLACKSTONE_BREAK, () -> SoundEvents.GILDED_BLACKSTONE_STEP, () -> SoundEvents.GILDED_BLACKSTONE_PLACE, () -> SoundEvents.GILDED_BLACKSTONE_HIT, () -> SoundEvents.GILDED_BLACKSTONE_FALL);
    }
}
