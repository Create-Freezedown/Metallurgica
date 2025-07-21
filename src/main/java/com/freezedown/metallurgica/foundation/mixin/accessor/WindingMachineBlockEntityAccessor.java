package com.freezedown.metallurgica.foundation.mixin.accessor;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WindingMachineBlockEntity.class, remap = false)
public interface WindingMachineBlockEntityAccessor {
    @Accessor("angle")
    float getAngle();
    @Accessor("spoolSpeed")
    LerpedFloat getSpoolSpeed();
}
