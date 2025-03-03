package com.freezedown.metallurgica.experimental;

import com.freezedown.metallurgica.content.entity.moltenShrapnel.MoltenShrapnelEntity;
import com.freezedown.metallurgica.registry.MetallurgicaEntityTypes;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class ExperimentalUtil {

    public void createSteamExplosion(Level level, BlockPos pos, Fluid moltenMetal, int radius, int count) {
        ResourceLocation fluid = ForgeRegistries.FLUIDS.getKey(moltenMetal);
        if (fluid == null) {
            return;
        }
        for(int i = 0; i < count; ++i) {
            float x = Create.RANDOM.nextFloat(360.0F);
            float y = Create.RANDOM.nextFloat(360.0F);
            float z = Create.RANDOM.nextFloat(360.0F);
            MoltenShrapnelEntity shrapnel = MetallurgicaEntityTypes.MOLTEN_SHRAPNEL.create(level);
            assert shrapnel != null;
            shrapnel.setFluid(fluid);
            shrapnel.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());
            float f = -Mth.sin(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
            float f1 = -Mth.sin((x + z) * 0.017453292F);
            float f2 = Mth.cos(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
            shrapnel.shoot(f, f1, f2, 0.6F, 1.0F);
            shrapnel.setBuiltUpVelocity(0.6F);
            level.addFreshEntity(shrapnel);
        }

        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), radius, Explosion.BlockInteraction.BREAK);
    }

}
