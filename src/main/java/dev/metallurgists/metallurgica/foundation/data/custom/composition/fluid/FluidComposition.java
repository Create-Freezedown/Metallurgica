package dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid;

import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
public record FluidComposition(FluidStack fluidStack, List<SubComposition> subCompositions) {
    public static final Codec<FluidStack> FLUIDSTACK_NO_AMOUNT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(BuiltInRegistries.FLUID.byNameCodec().fieldOf("name").forGetter(FluidStack::getFluid), CompoundTag.CODEC.optionalFieldOf("nbt").forGetter((stack) -> Optional.ofNullable(stack.getTag()))).apply(instance, (fluid, tag) -> {
        FluidStack stack = new FluidStack(fluid, 1);
        Objects.requireNonNull(stack);
        tag.ifPresent(stack::setTag);
        return stack;
    }));
    
    public static final Codec<FluidComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FLUIDSTACK_NO_AMOUNT_CODEC.fieldOf("fluid").forGetter(FluidComposition::fluidStack),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(FluidComposition::subCompositions)
    ).apply(instance, FluidComposition::new));
    
    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        subCompositions.forEach(element -> element.writeToPacket(buf));
    }
    
    public static FluidComposition fromNetwork(FriendlyByteBuf buf) {
        var fluidStack = buf.readFluidStack();
        var elements = SubComposition.listFromNetwork(buf);
        return new FluidComposition(fluidStack, elements);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject fluid = new JsonObject();
        fluid.addProperty("name", CatnipServices.REGISTRIES.getKeyOrThrow(fluidStack.getFluid()).toString());
        fluid.addProperty("nbt", fluidStack.getOrCreateTag().toString());
        json.add("fluid", fluid);
        JsonArray compositionsArray = new JsonArray();
        for (SubComposition subComposition : subCompositions) {
            compositionsArray.add(subComposition.toJson());
        }
        json.add("compositions", compositionsArray);
        return json;
    }
}
