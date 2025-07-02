package com.freezedown.metallurgica.compat.kubejs.builder.fluid;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.compat.kubejs.builder.item.AcidBarrelItemBuilder;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.open_ended_pipe.AcidHandler;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.content.fluids.VirtualFluid;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;

public class AcidFluidBuilder extends VirtualFluidBuilder {

    public transient int acidity = 7;
    public AcidBarrelItemBuilder barrelItem;

    public AcidFluidBuilder(ResourceLocation i) {
        super(i);
        tag(Metallurgica.asResource("acid"));
        noBucket();
        barrelItem = new AcidBarrelItemBuilder(this);
    }

    @Override
    public BuilderBase<FlowingFluid> displayName(Component name) {
        if (barrelItem != null) {
            barrelItem.displayName(Component.literal("").append(name).append(" Barrel"));
        }

        displayName = name;
        return this;
    }

    public AcidFluidBuilder acidity(int a) {
        this.acidity = a;
        return this;
    }

    @Override
    public Acid createObject() {
        var acid = Acid.createSource(createProperties()).acidity(this.acidity);
        registerOpenEndPipeEffectHandler();
        return acid;
    }

    @Override
    public void createAdditionalObjects() {
        RegistryInfo.FLUID.addBuilder(flowingFluid);

        if (barrelItem != null) {
            RegistryInfo.ITEM.addBuilder(barrelItem);
        }
    }

    public void registerOpenEndPipeEffectHandler() {
        var archFluidAttributes = this.createAttributes();
        if (archFluidAttributes == null) return;
        VirtualFluid virtualFluid = (VirtualFluid) archFluidAttributes.getSourceFluid();
        if (virtualFluid != null) {
            OpenPipeEffectHandler.REGISTRY.register(virtualFluid, new AcidHandler());
        }
    }
}
