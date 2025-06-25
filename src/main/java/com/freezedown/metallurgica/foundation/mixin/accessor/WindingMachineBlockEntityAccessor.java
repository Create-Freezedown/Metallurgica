package com.freezedown.metallurgica.foundation.mixin.accessor;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WindingMachineBlockEntity.class)
public interface WindingMachineBlockEntityAccessor {
    @Accessor("angle")
    float getAngle();
}
