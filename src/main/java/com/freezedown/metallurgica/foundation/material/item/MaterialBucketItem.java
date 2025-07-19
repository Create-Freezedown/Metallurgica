package com.freezedown.metallurgica.foundation.material.item;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MaterialBucketItem extends BucketItem {
    public final Material material;
    public final IFluidRegistry fluidFlag;

    public MaterialBucketItem(Supplier<? extends Fluid> supplier, Properties builder, Material material, IFluidRegistry flag) {
        super(supplier, builder);
        this.material = material;
        this.fluidFlag = flag;
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return this.getClass() == MaterialBucketItem.class ? new FluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
    }

    public String getUnlocalizedName() {
        return "materialflag.bucket." + fluidFlag.getKey();
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(), material.getDisplayName());
    }

    @Override
    public String getDescriptionId() {
        return getUnlocalizedName();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return getLocalizedName(material);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

}
