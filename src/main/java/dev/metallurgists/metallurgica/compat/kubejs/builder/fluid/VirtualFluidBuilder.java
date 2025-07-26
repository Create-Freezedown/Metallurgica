package dev.metallurgists.metallurgica.compat.kubejs.builder.fluid;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class VirtualFluidBuilder extends FluidBuilder {
    public ForgeFlowingFluid.Properties properties;

    public VirtualFluidBuilder(ResourceLocation i) {
        super(i);
        noBlock();
    }

    @Override
    public VirtualFluid createObject() {
        return new VirtualFluid(this.createProperties(), true);
    }

    @Override
    public BuilderBase<FlowingFluid> displayName(Component name) {
        if (bucketItem != null) {
            bucketItem.displayName(Component.literal("").append(name).append(" Bucket"));
        }

        displayName = name;
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        RegistryInfo.FLUID.addBuilder(flowingFluid);

        if (bucketItem != null) {
            RegistryInfo.ITEM.addBuilder(bucketItem);
        }
    }



    public ForgeFlowingFluid.Properties createProperties() {
        if (this.properties != null) {
            return this.properties;
        }
        var archFluidAttributes = this.createAttributes();

        var ftProps = FluidType.Properties.create()
                .density(archFluidAttributes.getDensity())
                .viscosity(archFluidAttributes.getViscosity())
                .temperature(archFluidAttributes.getTemperature())
                .lightLevel(archFluidAttributes.getLuminosity())
                .rarity(archFluidAttributes.getRarity());
        FluidType fluidType = this.color == 0xFFFFFF ? CreateRegistrate.defaultFluidType(ftProps, this.stillTexture, this.flowingTexture) : tintedFluidType(ftProps, this.stillTexture, this.flowingTexture, this.color);

        var properties = new ForgeFlowingFluid.Properties(() -> fluidType, archFluidAttributes::getSourceFluid, archFluidAttributes::getFlowingFluid);
        this.properties = properties;
        return properties;
    }

    public static FluidType tintedFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int color) {
        return new AllFluids.TintedFluidType(properties, stillTexture, flowingTexture) {
            @Override
            protected int getTintColor(FluidStack stack) {
                return color;
            }

            @Override
            protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return color;
            }
        };
    }

    @Override
    public String getBuilderTranslationKey() {
        if (translationKey.isEmpty()) {
            return "fluid_type." + id.getNamespace() + "." + id.getPath().replace('/', '.');
        }

        return translationKey;
    }
}
