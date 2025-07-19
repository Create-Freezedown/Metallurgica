package com.freezedown.metallurgica.infastructure.material.registry.flags.block;

import com.freezedown.metallurgica.foundation.material.block.AxisMaterialBlock;
import com.freezedown.metallurgica.foundation.material.block.IMaterialBlock;
import com.freezedown.metallurgica.foundation.material.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.material.block.MaterialBlockItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class StorageBlockFlag extends BlockFlag {
    @Getter
    private boolean requiresDecompacting;
    @Getter
    private boolean useColumnModel;

    public StorageBlockFlag(String existingNamespace) {
        super("%s_block", existingNamespace);
        this.setTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s"));
    }

    public StorageBlockFlag() {
        this("metallurgica");
    }

    public StorageBlockFlag requiresDecompacting() {
        this.requiresDecompacting = true;
        return this;
    }

    public StorageBlockFlag useColumnModel() {
        this.useColumnModel = true;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        NonNullFunction<BlockBehaviour.Properties, MaterialBlock> factory = useColumnModel ? p -> new AxisMaterialBlock(p, material, flag)
                : p -> new MaterialBlock(p, material, flag);
            return registrate.block(getIdPattern().formatted(material.getName()), factory)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.METAL))
                    .transform(pickaxeOnly())
                    .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .item(MaterialBlockItem::create)
                    .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                    .build()
                    .register();
    }

    @Override
    public boolean shouldHaveComposition() {
        return true;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.STORAGE_BLOCK;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
