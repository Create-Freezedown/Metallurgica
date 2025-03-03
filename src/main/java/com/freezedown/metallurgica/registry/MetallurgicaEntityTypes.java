package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.entity.moltenShrapnel.MoltenShrapnelEntity;
import com.freezedown.metallurgica.content.entity.moltenShrapnel.MoltenShrapnelRenderer;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.lang.Lang;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MetallurgicaEntityTypes {
    public static final EntityEntry<MoltenShrapnelEntity> MOLTEN_SHRAPNEL;

    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory, NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer, MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire, NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
        String id = Lang.asId(name);
        return (CreateEntityBuilder<T, ?>) Metallurgica.registrate().entity(id, factory, group).properties((b) -> {
            b.setTrackingRange(range).setUpdateInterval(updateFrequency).setShouldReceiveVelocityUpdates(sendVelocity);
        }).properties(propertyBuilder).properties((b) -> {
            if (immuneToFire) {
                b.fireImmune();
            }

        }).renderer(renderer);
    }

    public static void register() {
    }

    static {
        MOLTEN_SHRAPNEL = register("molten_shrapnel", MoltenShrapnelEntity::new, () -> MoltenShrapnelRenderer::new, MobCategory.MISC, 4, 20, true, true, MoltenShrapnelEntity::build).register();
    }
}
