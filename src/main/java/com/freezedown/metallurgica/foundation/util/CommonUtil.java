package com.freezedown.metallurgica.foundation.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonUtil {
    
    public static String getItemIdentifier(Item item) {
        String unwrappedHolder = ForgeRegistries.ITEMS.getHolder(item).flatMap(Holder::unwrapKey).toString();
        String prunedKey = unwrappedHolder.replace("Optional[ResourceKey[minecraft:item / ", "").replace("]]", "");
        return prunedKey;
    }
    
    public static String getFluidIdentifier(FluidStack fluid) {
        String unwrappedHolder = ForgeRegistries.FLUIDS.getHolder(fluid.getFluid()).flatMap(Holder::unwrapKey).toString();
        String prunedKey = unwrappedHolder.replace("Optional[ResourceKey[minecraft:fluid / ", "").replace("]]", "");
        return prunedKey;
    }
    
    public static ResourceLocation getItemIdentifierAsResourceLocation(Item item) {
        return new ResourceLocation(getItemIdentifier(item));
    }
    
    public static boolean isClassFound(String className) {
        try {
            Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static boolean isRainingAt(BlockPos pos, Level world) {
        if (!world.isRaining()) {
            return false;
        } else if (!world.canSeeSky(pos)) {
            return false;
        } else if (world.getHeight() > pos.getY()) {
            return false;
        } else {
            return true;
        }
    }
    
    public static MobEffectInstance noHeal(MobEffectInstance ei) {
        ei.setCurativeItems(ImmutableList.of());
        return ei;
    }
}
