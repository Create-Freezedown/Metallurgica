package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class StorageBlockFlag extends BlockFlag {
    @Getter
    private boolean requiresCompressing;

    public StorageBlockFlag() {
        super("%s_block");
    }

    public StorageBlockFlag(String existingNamespace) {
        super("%s_block", existingNamespace);
    }

    public StorageBlockFlag requiresCompressing(boolean requiresCompressing) {
        this.requiresCompressing = requiresCompressing;
        return this;
    }

    @Override
    public BlockEntry<? extends MaterialBlock> registerBlock(@NotNull Material material, BlockFlag flag, @NotNull MetallurgicaRegistrate registrate) {
            return registrate.block(getIdPattern().formatted(material.getName()), (p) -> new MaterialBlock(p, material, flag))
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.METAL))
                    .transform(pickaxeOnly())
                    .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .item()
                    .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                    .build()
                    .register();
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
