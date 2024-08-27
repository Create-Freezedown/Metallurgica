package com.freezedown.metallurgica.foundation.data.custom.composition.fluid;

import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record FluidComposition(FluidStack fluidStack, List<Element> elements) {
    public static final Codec<FluidStack> FLUIDSTACK_NO_AMOUNT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(Registry.FLUID.byNameCodec().fieldOf("name").forGetter(FluidStack::getFluid), CompoundTag.CODEC.optionalFieldOf("nbt").forGetter((stack) -> Optional.ofNullable(stack.getTag()))).apply(instance, (fluid, tag) -> {
        FluidStack stack = new FluidStack(fluid, 1);
        Objects.requireNonNull(stack);
        tag.ifPresent(stack::setTag);
        return stack;
    }));
    
    public static final Codec<FluidComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FLUIDSTACK_NO_AMOUNT_CODEC.fieldOf("fluid").forGetter(FluidComposition::fluidStack),
            Codec.list(Element.CODEC).fieldOf("elements").forGetter(FluidComposition::elements)
    ).apply(instance, FluidComposition::new));
}
