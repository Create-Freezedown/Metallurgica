package dev.metallurgists.metallurgica.foundation.mixin.accessor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KineticBlockEntity.class, remap = false)
public interface KineticBlockEntityAccessor {
    @Accessor("stress")
    float getStress();
    @Accessor("capacity")
    float getMaxStress();
}
