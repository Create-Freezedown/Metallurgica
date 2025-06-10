package com.freezedown.metallurgica.foundation.item;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class MaterialBucketItem extends BucketItem {
    public final Material material;
    private final BucketType bucketType;

    public MaterialBucketItem(Supplier<? extends Fluid> supplier, Properties builder, Material material, BucketType bucketType) {
        super(supplier, builder);
        this.material = material;
        this.bucketType = bucketType;
    }

    public MaterialBucketItem(Supplier<? extends Fluid> supplier, Properties builder, Material material) {
        this(supplier, builder, material, BucketType.FLUID);
    }

    public String getUnlocalizedName() {
        return "materialflag.bucket" + bucketType.getLangSuffix();
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(), material.getLocalizedName());
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

    public enum BucketType {
        FLUID(""),
        MOLTEN(".molten")
        ;

        private final String langSuffix;

        BucketType(String langSuffix) {
            this.langSuffix = langSuffix;
        }

        String getLangSuffix() {
            return langSuffix;
        }

    }
}
