package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public abstract class BlockFlag implements IMaterialFlag {

    private final String idPattern;
    private String existingNamespace = "metallurgica";
    @Setter
    private List<String> tagPatterns = List.of();
    @Setter
    private List<String> itemTagPatterns = List.of();

    public BlockFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public BlockFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public String getUnlocalizedName() {
        return "materialflag." + (this instanceof ISpecialLangSuffix suffix ? suffix.getLangSuffix() : MetallurgicaModels.getFlagName(getKey()));
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getLocalizedName());
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModid(), this.idPattern.formatted(material.getName()));
        if (TextUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    public ResourceLocation getExistingId(Material material, FlagKey<?> flagKey) {
        String nameAlternative = material.materialInfo().nameAlternatives().get(flagKey);
        if (nameAlternative != null) {
            return new ResourceLocation(existingNamespace, idPattern.formatted(nameAlternative));
        }
        return new ResourceLocation(existingNamespace, idPattern.formatted(material.getName()));
    }

    public abstract BlockEntry<? extends MaterialBlock> registerBlock(@NotNull Material material, BlockFlag flag, @NotNull MetallurgicaRegistrate registrate);

    public abstract boolean shouldHaveComposition();

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
