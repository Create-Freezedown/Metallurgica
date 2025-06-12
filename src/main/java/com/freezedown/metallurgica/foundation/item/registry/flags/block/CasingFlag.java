package com.freezedown.metallurgica.foundation.item.registry.flags.block;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.block.MaterialBlockItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaSpriteShifts;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CasingFlag extends BlockFlag implements IHaveConnectedTextures{

    @Getter
    private List<TagKey<Item>> toApplyOn = List.of(AllTags.AllItemTags.STRIPPED_LOGS.tag, AllTags.AllItemTags.STRIPPED_WOOD.tag);
    @Getter
    private boolean useSheet = false;

    public CasingFlag(String existingNamespace) {
        super("%s_casing", existingNamespace);
        this.setTagPatterns(List.of("metallurgica:casings", "metallurgica:casings/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("metallurgica:casings", "metallurgica:casings/%s"));
    }


    public CasingFlag() {
        this("metallurgica");
    }

    @SafeVarargs
    public final CasingFlag appliesOn(TagKey<Item>... tags) {
        this.toApplyOn = List.of(tags);
        return this;
    }

    public CasingFlag useSheet() {
        this.useSheet = true;
        return this;
    }

    @Override
    public BlockEntry<? extends MaterialBlock> registerBlock(@NotNull Material material, BlockFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        CTSpriteShiftEntry spriteShift = getSpriteShiftEntry(material);
        return registrate.block(getIdPattern().formatted(material.getName()), (p) -> new MaterialBlock(p, material, flag))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.METAL))
                .transform(pickaxeOnly())
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item(MaterialBlockItem::create)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(spriteShift)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, spriteShift)))
                .register();
    }

    @Override
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.CASING;
    }

    @Override
    public CTSpriteShiftEntry getSpriteShiftEntry(Material material) {
        return MetallurgicaSpriteShifts.materialOmni(material, getKey());
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (useSheet) {
            flags.ensureSet(FlagKey.SHEET, true);
        } else {
            flags.ensureSet(FlagKey.INGOT, true);
        }
    }
}
